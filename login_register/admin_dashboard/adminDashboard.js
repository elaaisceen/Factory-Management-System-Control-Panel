let currentDept = '';

// Toast bildirimi
function showToast(message, type) {
    const existing = document.getElementById('adminToast');
    if (existing) existing.remove();
    const toast = document.createElement('div');
    toast.id = 'adminToast';
    const bg = type === 'error' ? 'bg-red-600' : 'bg-slate-900';
    toast.className = `fixed top-24 right-6 ${bg} text-white text-sm font-semibold px-5 py-3 rounded-xl shadow-2xl z-[200] flex items-center gap-2 transition-all animate-bounce`;
    toast.innerHTML = `<span class="material-symbols-outlined text-base">${type === 'error' ? 'error' : 'check_circle'}</span> ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 2800);
}

// Helper to log system events
function logSystemEvent(user, action, location, status) {
    const logs = JSON.parse(localStorage.getItem('systemLogs')) || [];
    logs.unshift({
        user: user,
        action: action,
        location: location,
        time: new Date().toISOString(),
        status: status
    });
    localStorage.setItem('systemLogs', JSON.stringify(logs.slice(0, 50))); // Keep last 50 logs
}

function updateDashboard() {
    // 1. Production Stats
    const prodDb = JSON.parse(localStorage.getItem('productionDatabase')) || {};
    let totalProd = 142800; // Base value
    let activeUnits = 0;
    let totalUnits = 0;
    Object.values(prodDb).forEach(deptArr => {
        deptArr.forEach(item => {
            totalUnits++;
            if (item.status === 'Aktif') activeUnits++;
        });
    });
    const prodEl = document.querySelector('h3.text-primary');
    if (prodEl) prodEl.innerText = ((totalProd + totalUnits * 100) / 1000).toFixed(1) + 'k';

    // 2. Finance Stats
    const finDb = JSON.parse(localStorage.getItem('financeDatabase')) || [];
    let totalRevenue = 2400000; // Base value
    finDb.forEach(item => {
        if (item.status === 'Ödendi') totalRevenue += parseFloat(item.amount || 0);
    });
    const revEl = document.querySelector('h3.text-tertiary');
    if (revEl) revEl.innerText = '$' + (totalRevenue / 1000000).toFixed(1) + 'M';

    // 3. HR Stats
    const hrDb = JSON.parse(localStorage.getItem('factory_personnel')) || [];
    let totalHR = 842 + hrDb.length;
    const hrEl = document.querySelector('h3.text-secondary');
    if (hrEl) hrEl.innerText = totalHR;

    // 4. IT / Alerts Stats
    const itDb = JSON.parse(localStorage.getItem('it_support_tickets')) || [];
    const pendingTickets = itDb.filter(t => t.status === 'Beklemede').length;
    const alertEl = document.querySelector('h3.text-error');
    if (alertEl) alertEl.innerText = String(pendingTickets).padStart(2, '0');

    // 5. Activity Logs Table
    const logs = JSON.parse(localStorage.getItem('systemLogs')) || [];
    const tbody = document.querySelector('tbody');
    if (tbody && logs.length > 0) {
        tbody.innerHTML = logs.slice(0, 10).map(log => {
            const date = new Date(log.time);
            const timeAgo = Math.floor((new Date() - date) / 60000);
            const timeStr = timeAgo < 1 ? 'Az önce' : (timeAgo < 60 ? `${timeAgo} dk önce` : `${Math.floor(timeAgo / 60)} saat önce`);
            const initials = log.user.split(' ').map(n => n[0]).join('');

            return `
                <tr class="hover:bg-slate-50 transition-colors group">
                    <td class="px-8 py-4">
                        <div class="flex items-center gap-3">
                            <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-primary font-bold text-xs">${initials}</div>
                            <span class="text-sm font-semibold">${log.user}</span>
                        </div>
                    </td>
                    <td class="px-8 py-4 text-sm">${log.action}</td>
                    <td class="px-8 py-4 text-xs text-slate-500">${log.location}</td>
                    <td class="px-8 py-4 text-xs text-slate-500">${timeStr}</td>
                    <td class="px-8 py-4 text-right">
                        <span class="bg-secondary/10 text-secondary px-3 py-1 rounded-full text-[10px] font-bold">${log.status}</span>
                    </td>
                </tr>
            `;
        }).join('');
    }
}


function openDeptModal(dept) {
    currentDept = dept;
    const modal = document.getElementById('deptModal');
    const title = document.getElementById('modalTitle');
    const fields = document.getElementById('modalFormFields');
    const menu = document.getElementById('newDataMenu');

    if (menu) menu.classList.add('hidden'); // Close dropdown
    fields.innerHTML = ''; // Clear previous fields
    modal.classList.remove('hidden');

    switch (dept) {
        case 'production':
            title.innerText = 'Yeni Üretim Operasyonu';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Alt Departman</label>
                    <select id="prodSubDept" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                        <option value="montaj">Montaj</option>
                        <option value="paketleme">Paketleme</option>
                        <option value="kalite_kontrol">Kalite Kontrol</option>
                        <option value="planlama_tasarim">Planlama / Tasarım</option>
                    </select>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Makine / İstasyon Adı</label>
                    <input type="text" id="prodName" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: CNC-01" required>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Durum</label>
                        <select id="prodStatus" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="Aktif">Aktif</option>
                            <option value="Pasif">Pasif</option>
                            <option value="Bakımda">Bakımda</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Performans (%)</label>
                        <input type="number" id="prodPerf" min="0" max="100" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="0-100" required>
                    </div>
                </div>`;
            break;
        case 'hr':
            title.innerText = 'Yeni Personel Kaydı';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Personel Adı Soyadı</label>
                    <input type="text" id="hrName" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Sarah Connor" required>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Rol / Pozisyon</label>
                    <input type="text" id="hrRole" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Supervisor" required>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Durum</label>
                        <select id="hrStatus" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="On-Shift">On-Shift</option>
                            <option value="Remote">Remote</option>
                            <option value="On Leave">On Leave</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Puan (0-5)</label>
                        <input type="number" id="hrRating" step="0.1" min="0" max="5" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="4.5" required>
                    </div>
                </div>`;
            break;
        case 'finance':
            title.innerText = 'Yeni Finansal İşlem';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Şirket / Alıcı</label>
                    <input type="text" id="finCompany" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Steel Dynamics" required>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">İşlem Detayı</label>
                    <input type="text" id="finDetail" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Hammadde Alımı" required>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Tutar ($)</label>
                        <input type="number" id="finAmount" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="1000" required>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Durum</label>
                        <select id="finStatus" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="Ödendi">Ödendi</option>
                            <option value="İşleniyor">İşleniyor</option>
                            <option value="Gecikmiş">Gecikmiş</option>
                        </select>
                    </div>
                </div>`;
            break;
        case 'stock':
            title.innerText = 'Yeni Stok Girişi';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Kaynak / Ürün Adı</label>
                    <input type="text" id="stockName" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Alüminyum Bobin" required>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Stok Kodu (SKU)</label>
                        <input type="text" id="stockSku" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="SKU-001" required>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Durum</label>
                        <select id="stockStatus" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="Sağlıklı">Sağlıklı</option>
                            <option value="Sipariş Verilmeli">Sipariş Verilmeli</option>
                            <option value="Kritik Düşük">Kritik Düşük</option>
                        </select>
                    </div>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Miktar</label>
                        <input type="number" id="stockQty" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Birim</label>
                        <select id="stockUnit" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="adet kaldı">Adet</option>
                            <option value="kg kaldı">Kilogram (kg)</option>
                            <option value="Litre kaldı">Litre (L)</option>
                        </select>
                    </div>
                </div>`;
            break;
        case 'it':
            title.innerText = 'Yeni IT Destek Talebi';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Talep Konusu</label>
                    <input type="text" id="itSubject" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Ağ Sorunu" required>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Talep Eden</label>
                    <input type="text" id="itRequester" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Alex" required>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Öncelik</label>
                    <select id="itPriority" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                        <option value="High">Yüksek</option>
                        <option value="Medium">Orta</option>
                        <option value="Low">Düşük</option>
                    </select>
                </div>`;
            break;
        case 'purchasing':
            title.innerText = 'Yeni Satın Alma Siparişi';
            fields.innerHTML = `
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Tedarikçi</label>
                    <input type="text" id="pureSupplier" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Stark Ind." required>
                </div>
                <div>
                    <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Ürün / Kalem</label>
                    <input type="text" id="pureItem" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" placeholder="Örn: Çelik Bobin" required>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Miktar</label>
                        <input type="number" id="pureQty" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                    </div>
                    <div>
                        <label class="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1.5 px-1">Durum</label>
                        <select id="pureStatus" class="w-full rounded-xl border-slate-200 text-sm p-3 font-medium outline-none focus:ring-2 focus:ring-primary/20" required>
                            <option value="Pending">Beklemede</option>
                            <option value="On Way">Yolda</option>
                            <option value="Quality Inspection">Kalite Kontrol</option>
                        </select>
                    </div>
                </div>`;
            break;
    }
}

function closeDeptModal() {
    document.getElementById('deptModal').classList.add('hidden');
}

document.addEventListener('DOMContentLoaded', function () {
    // Dropdown toggle logic
    const newDataBtn = document.getElementById('newDataBtn');
    const newDataMenu = document.getElementById('newDataMenu');

    if (newDataBtn && newDataMenu) {
        newDataBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            newDataMenu.classList.toggle('hidden');
        });

        document.addEventListener('click', function () {
            newDataMenu.classList.add('hidden');
        });

        // Initialize dashboard
        updateDashboard();
    }

    // Form Submit Logic
    const form = document.getElementById('deptModalForm');
    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const timestamp = Date.now();
            const dateStr = new Date().toLocaleDateString('tr-TR', { day: 'numeric', month: 'short', year: 'numeric' });

            try {
                switch (currentDept) {
                    case 'production':
                        const subDept = document.getElementById('prodSubDept').value;
                        const prodDb = JSON.parse(localStorage.getItem('productionDatabase')) || { montaj: [], paketleme: [], kalite_kontrol: [], planlama_tasarim: [] };
                        if (!prodDb[subDept]) prodDb[subDept] = [];
                        const prodName = document.getElementById('prodName').value;
                        prodDb[subDept].push({
                            id: timestamp,
                            name: prodName,
                            status: document.getElementById('prodStatus').value,
                            performance: document.getElementById('prodPerf').value
                        });
                        localStorage.setItem('productionDatabase', JSON.stringify(prodDb));
                        logSystemEvent('Alex Sterling', `New Production: ${prodName}`, 'Production Sector', 'SUCCESS');
                        break;
                    case 'hr':
                        const hrKey = 'factory_personnel';
                        const hrDbArr = JSON.parse(localStorage.getItem(hrKey) || '[]');
                        const pName = document.getElementById('hrName').value;
                        hrDbArr.push({
                            id: timestamp,
                            name: pName,
                            dept: document.getElementById('hrRole').value,
                            expert: 'Yeni Kayıt',
                            status: 'active'
                        });
                        localStorage.setItem(hrKey, JSON.stringify(hrDbArr));
                        logSystemEvent('Alex Sterling', `New Employee: ${pName}`, 'HR Office', 'SUCCESS');
                        break;
                    case 'finance':
                        const finDb = JSON.parse(localStorage.getItem('financeDatabase')) || [];
                        const finComp = document.getElementById('finCompany').value;
                        finDb.push({
                            id: 'TXN-' + timestamp.toString().slice(-5),
                            company: finComp,
                            detail: document.getElementById('finDetail').value,
                            amount: document.getElementById('finAmount').value,
                            status: document.getElementById('finStatus').value,
                            date: dateStr
                        });
                        localStorage.setItem('financeDatabase', JSON.stringify(finDb));
                        logSystemEvent('Alex Sterling', `Finance Transaction: ${finComp}`, 'Finance Dept', 'SUCCESS');
                        break;
                    case 'stock':
                        const stockDb = JSON.parse(localStorage.getItem('stockDatabase')) || [];
                        const stockName = document.getElementById('stockName').value;
                        stockDb.push({
                            id: timestamp,
                            name: stockName,
                            sku: document.getElementById('stockSku').value.toUpperCase(),
                            qty: document.getElementById('stockQty').value,
                            unit: document.getElementById('stockUnit').value,
                            status: document.getElementById('stockStatus').value
                        });
                        localStorage.setItem('stockDatabase', JSON.stringify(stockDb));
                        logSystemEvent('Alex Sterling', `Stock Update: ${stockName}`, 'Warehouse', 'SUCCESS');
                        break;
                    case 'it':
                        const itKey = 'it_support_tickets';
                        const itDbArr = JSON.parse(localStorage.getItem(itKey) || '[]');
                        const itSubject = document.getElementById('itSubject').value;
                        itDbArr.push({
                            id: timestamp,
                            title: itSubject,
                            requester: document.getElementById('itRequester').value,
                            dept: 'Yönetim',
                            status: 'Beklemede',
                            taskStatus: '0',
                            assignee: 'unassigned'
                        });
                        localStorage.setItem(itKey, JSON.stringify(itDbArr));
                        logSystemEvent('Alex Sterling', `IT Ticket: ${itSubject}`, 'IT Desk', 'SUCCESS');
                        break;
                    case 'purchasing':
                        const pureDb = JSON.parse(localStorage.getItem('purchasingDatabase')) || [];
                        const pureItem = document.getElementById('pureItem').value;
                        pureDb.push({
                            id: 'PO-' + timestamp.toString().slice(-4),
                            supplier: document.getElementById('pureSupplier').value,
                            item: pureItem,
                            qty: document.getElementById('pureQty').value,
                            status: document.getElementById('pureStatus').value,
                            eta: 'Pending'
                        });
                        localStorage.setItem('purchasingDatabase', JSON.stringify(pureDb));
                        logSystemEvent('Alex Sterling', `Purchasing: ${pureItem}`, 'Purchasing', 'SUCCESS');
                        break;
                }

                showToast('Kayıt başarıyla eklendi!');
                updateDashboard();
                closeDeptModal();
                form.reset();
            } catch (error) {
                console.error('Save error:', error);
                showToast('Kayıt sırasında bir hata oluştu.', 'error');
            }
        });
    }
});
