
let currentDept = '';

function openDeptModal(dept) {
    currentDept = dept;
    const modal = document.getElementById('deptModal');
    const title = document.getElementById('modalTitle');
    const fields = document.getElementById('modalFormFields');
    const menu = document.getElementById('newDataMenu');
    
    if (menu) menu.classList.add('hidden'); // Close dropdown
    fields.innerHTML = ''; // Clear previous fields
    modal.classList.remove('hidden');

    switch(dept) {
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

document.addEventListener('DOMContentLoaded', function() {
    // Dropdown toggle logic
    const newDataBtn = document.getElementById('newDataBtn');
    const newDataMenu = document.getElementById('newDataMenu');

    if (newDataBtn && newDataMenu) {
        newDataBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            newDataMenu.classList.toggle('hidden');
        });

        document.addEventListener('click', function() {
            newDataMenu.classList.add('hidden');
        });
    }

    // Form Submit Logic
    const form = document.getElementById('deptModalForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const timestamp = Date.now();
            const dateStr = new Date().toLocaleDateString('tr-TR', { day: 'numeric', month: 'short', year: 'numeric' });

            try {
                switch(currentDept) {
                    case 'production':
                        const subDept = document.getElementById('prodSubDept').value;
                        const prodDb = JSON.parse(localStorage.getItem('productionDatabase')) || { montaj: [], paketleme: [], kalite_kontrol: [], planlama_tasarim: [] };
                        if (!prodDb[subDept]) prodDb[subDept] = [];
                        prodDb[subDept].push({
                            id: timestamp,
                            name: document.getElementById('prodName').value,
                            status: document.getElementById('prodStatus').value,
                            performance: document.getElementById('prodPerf').value
                        });
                        localStorage.setItem('productionDatabase', JSON.stringify(prodDb));
                        break;
                    case 'hr':
                        const hrDb = JSON.parse(localStorage.getItem('hrDatabase')) || [];
                        hrDb.push({
                            id: timestamp,
                            name: document.getElementById('hrName').value,
                            role: document.getElementById('hrRole').value,
                            status: document.getElementById('hrStatus').value,
                            rating: document.getElementById('hrRating').value,
                            date: 'Just now'
                        });
                        localStorage.setItem('hrDatabase', JSON.stringify(hrDb));
                        break;
                    case 'finance':
                        const finDb = JSON.parse(localStorage.getItem('financeDatabase')) || [];
                        finDb.push({
                            id: 'TXN-' + timestamp.toString().slice(-5),
                            company: document.getElementById('finCompany').value,
                            detail: document.getElementById('finDetail').value,
                            amount: document.getElementById('finAmount').value,
                            status: document.getElementById('finStatus').value,
                            date: dateStr
                        });
                        localStorage.setItem('financeDatabase', JSON.stringify(finDb));
                        break;
                    case 'stock':
                        const stockDb = JSON.parse(localStorage.getItem('stockDatabase')) || [];
                        stockDb.push({
                            id: timestamp,
                            name: document.getElementById('stockName').value,
                            sku: document.getElementById('stockSku').value.toUpperCase(),
                            qty: document.getElementById('stockQty').value,
                            unit: document.getElementById('stockUnit').value,
                            status: document.getElementById('stockStatus').value
                        });
                        localStorage.setItem('stockDatabase', JSON.stringify(stockDb));
                        break;
                    case 'it':
                        const itDb = JSON.parse(localStorage.getItem('itDatabase')) || [];
                        itDb.push({
                            id: timestamp,
                            subject: document.getElementById('itSubject').value,
                            requester: document.getElementById('itRequester').value,
                            priority: document.getElementById('itPriority').value,
                            time: 'Just now'
                        });
                        localStorage.setItem('itDatabase', JSON.stringify(itDb));
                        break;
                    case 'purchasing':
                        const pureDb = JSON.parse(localStorage.getItem('purchasingDatabase')) || [];
                        pureDb.push({
                            id: 'PO-' + timestamp.toString().slice(-4),
                            supplier: document.getElementById('pureSupplier').value,
                            item: document.getElementById('pureItem').value,
                            qty: document.getElementById('pureQty').value,
                            status: document.getElementById('pureStatus').value,
                            eta: 'Pending'
                        });
                        localStorage.setItem('purchasingDatabase', JSON.stringify(pureDb));
                        break;
                }

                alert('Kayıt başarıyla eklendi!');
                closeDeptModal();
                form.reset();
            } catch (error) {
                console.error('Save error:', error);
                alert('Kayıt sırasında bir hata oluştu.');
            }
        });
    }
});
