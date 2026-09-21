const state = { books: [], authors: [], members: [], section: 'overview', editing: null };
const labels = { books: 'book', authors: 'author', members: 'member' };
const fields = {
    books: [['title', 'Title', 'text'], ['price', 'Price', 'number'], ['authorName', 'Author name', 'text']],
    authors: [['name', 'Name', 'text'], ['country', 'Country', 'text']],
    members: [['name', 'Name', 'text'], ['mail', 'Email', 'email'], ['phoneNumber', 'Phone number', 'tel']]
};

const byId = id => document.getElementById(id);
const escapeHtml = value => String(value ?? '').replace(/[&<>"']/g, char => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' }[char]));

async function api(path, options = {}) {
    const response = await fetch(path, { headers: { 'Content-Type': 'application/json' }, ...options });
    if (!response.ok) throw new Error(await response.text() || 'Request failed');
    return response.status === 204 ? null : response.json();
}

async function loadData() {
    try {
        const [books, authors, members] = await Promise.all([api('/books'), api('/authors'), api('/members')]);
        state.books = books; state.authors = authors; state.members = members;
        renderAll();
    } catch (error) {
        showToast('Could not load library data');
        console.error(error);
    }
}

function renderAll() {
    byId('book-count').textContent = state.books.length;
    byId('author-count').textContent = state.authors.length;
    byId('member-count').textContent = state.members.length;
    renderBooks(); renderAuthors(); renderMembers(); renderRecentBooks();
}

function renderRecentBooks() {
    const recent = state.books.slice(-4).reverse();
    byId('recent-books').innerHTML = recent.length ? recent.map(book => `<div class="mini-row"><strong>${escapeHtml(book.title)}</strong><small>${escapeHtml(book.author?.name || 'Unknown author')}</small></div>`).join('') : '<div class="empty-state">No books catalogued yet.</div>';
}

function renderBooks() {
    const query = byId('books-search').value.toLowerCase();
    const rows = state.books.filter(book => `${book.title} ${book.author?.name || ''}`.toLowerCase().includes(query));
    byId('books-result-count').textContent = `${rows.length} record${rows.length === 1 ? '' : 's'}`;
    byId('books-table').innerHTML = rows.length ? rows.map(book => `<tr><td><strong>${escapeHtml(book.title)}</strong></td><td class="muted">${escapeHtml(book.author?.name || 'Unassigned')}</td><td class="muted">${Number(book.price || 0).toFixed(2)}</td><td>${actions('books', book.id)}</td></tr>`).join('') : emptyRow(4, 'No books match your search.');
}

function renderAuthors() {
    const query = byId('authors-search').value.toLowerCase();
    const rows = state.authors.filter(author => `${author.name} ${author.country || ''}`.toLowerCase().includes(query));
    byId('authors-result-count').textContent = `${rows.length} record${rows.length === 1 ? '' : 's'}`;
    byId('authors-table').innerHTML = rows.length ? rows.map(author => `<tr><td><strong>${escapeHtml(author.name)}</strong></td><td class="muted">${escapeHtml(author.country || '-')}</td><td>${actions('authors', author.id)}</td></tr>`).join('') : emptyRow(3, 'No authors match your search.');
}

function renderMembers() {
    const query = byId('members-search').value.toLowerCase();
    const rows = state.members.filter(member => `${member.name} ${member.mail} ${member.phoneNumber}`.toLowerCase().includes(query));
    byId('members-result-count').textContent = `${rows.length} record${rows.length === 1 ? '' : 's'}`;
    byId('members-table').innerHTML = rows.length ? rows.map(member => `<tr><td><strong>${escapeHtml(member.name)}</strong></td><td class="muted">${escapeHtml(member.mail)}</td><td class="muted">${escapeHtml(member.phoneNumber)}</td><td>${actions('members', member.id)}</td></tr>`).join('') : emptyRow(4, 'No members match your search.');
}

function actions(type, id) { return `<div class="row-actions"><button class="icon-button" title="Edit" data-edit="${type}" data-id="${id}">Edit</button><button class="icon-button delete" title="Delete" data-delete="${type}" data-id="${id}">Del</button></div>`; }
function emptyRow(columns, text) { return `<tr><td colspan="${columns}" class="empty-state">${text}</td></tr>`; }

function switchSection(section) {
    state.section = section;
    document.querySelectorAll('.nav-item').forEach(button => button.classList.toggle('active', button.dataset.section === section));
    document.querySelectorAll('.page-section').forEach(page => page.classList.toggle('active-section', page.id === `${section}-section`));
    byId('page-title').textContent = section === 'overview' ? 'Good morning, librarian.' : `${section[0].toUpperCase()}${section.slice(1)}`;
    byId('header-action').textContent = section === 'overview' ? '+ Add book' : `+ Add ${labels[section]}`;
}

function openModal(type, record = null) {
    state.editing = { type, id: record?.id || null };
    byId('modal-kicker').textContent = record ? `Edit ${labels[type]}` : 'New record';
    byId('modal-title').textContent = `${record ? 'Edit' : 'Add'} ${labels[type]}`;
    byId('form-error').textContent = '';
    byId('form-fields').innerHTML = fields[type].map(([name, label, inputType]) => `<div class="field"><label for="field-${name}">${label}</label><input id="field-${name}" name="${name}" type="${inputType}" step="0.01" required value="${escapeHtml(record?.[name] ?? (name === 'authorName' ? record?.author?.name : ''))}"></div>`).join('');
    byId('modal-backdrop').hidden = false;
    byId(`field-${fields[type][0][0]}`).focus();
}

function closeModal() { byId('modal-backdrop').hidden = true; state.editing = null; }

async function saveRecord(event) {
    event.preventDefault();
    const { type, id } = state.editing;
    const data = Object.fromEntries(new FormData(event.target).entries());
    if (type === 'books') data.price = Number(data.price);
    try {
        if (id) {
            if (type === 'books') data.author = state.authors.find(author => author.name.toLowerCase() === data.authorName.toLowerCase());
            delete data.authorName;
            await api(`/${type}/${id}`, { method: 'PUT', body: JSON.stringify(data) });
        } else {
            await api(`/${type}`, { method: 'POST', body: JSON.stringify(data) });
        }
        closeModal(); await loadData(); showToast(`${labels[type][0].toUpperCase()}${labels[type].slice(1)} saved`);
    } catch (error) { byId('form-error').textContent = error.message.includes('Author') ? 'That author could not be found.' : 'Could not save this record.'; }
}

async function deleteRecord(type, id) {
    if (!confirm(`Delete this ${labels[type]}?`)) return;
    try { await api(`/${type}/${id}`, { method: 'DELETE' }); await loadData(); showToast(`${labels[type][0].toUpperCase()}${labels[type].slice(1)} deleted`); } catch (error) { showToast('Could not delete this record'); }
}

function showToast(message) { const toast = byId('toast'); toast.textContent = message; toast.classList.add('show'); setTimeout(() => toast.classList.remove('show'), 2400); }

document.addEventListener('click', event => {
    const nav = event.target.closest('[data-section]'); if (nav) switchSection(nav.dataset.section);
    const sectionLink = event.target.closest('[data-section-link]'); if (sectionLink) switchSection(sectionLink.dataset.sectionLink);
    const quick = event.target.closest('[data-quick]'); if (quick) { switchSection(quick.dataset.quick); openModal(quick.dataset.quick); }
    const add = event.target.closest('[data-add]'); if (add) openModal(add.dataset.add);
    if (event.target.id === 'header-action') openModal(state.section === 'overview' ? 'books' : state.section);
    const edit = event.target.closest('[data-edit]'); if (edit) openModal(edit.dataset.edit, state[edit.dataset.edit].find(item => String(item.id) === edit.dataset.id));
    const del = event.target.closest('[data-delete]'); if (del) deleteRecord(del.dataset.delete, del.dataset.id);
});
['books', 'authors', 'members'].forEach(type => byId(`${type}-search`).addEventListener('input', () => ({ books: renderBooks, authors: renderAuthors, members: renderMembers }[type])()));
byId('record-form').addEventListener('submit', saveRecord); byId('close-modal').addEventListener('click', closeModal); byId('cancel-modal').addEventListener('click', closeModal);
byId('modal-backdrop').addEventListener('click', event => { if (event.target.id === 'modal-backdrop') closeModal(); });
loadData();
