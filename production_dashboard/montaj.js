document.addEventListener('DOMContentLoaded', () => {
    const DEPARTMENT_KEY = 'montaj';

    // Veritabanı yapısını yükle veya oluştur
    let db = JSON.parse(localStorage.getItem('productionDatabase')) || {
        montaj: [], paketleme: [], kalite_kontrol: [], planlama_tasarim: []
    };

    // Eğer önceki sürümlerde böyle bir obje yoksa düzelt
    if(Array.isArray(db)) {
        db = { montaj: [], paketleme: [], kalite_kontrol: [], planlama_tasarim: [] };
    }

    const tableBody = document.getElementById('dataTableBody');
    const form = document.getElementById('deptForm');
    const countBadge = document.getElementById('recordCount');

    // Renk Çözücü
    const getStatusStyle = (status) => {
        if(status === 'Aktif') return 'bg-secondary-container text-on-secondary-container';
        if(status === 'Pasif') return 'bg-error-container text-on-error-container';
        if(status === 'Bakımda') return 'bg-orange-100 text-orange-800';
        return 'bg-slate-100 text-slate-800';
    };

    const getPerfColor = (val) => {
        if(val >= 85) return 'text-secondary';
        if(val >= 50) return 'text-orange-500';
        return 'text-error';
    };

    // Tabloyu Çizdir
    const renderTable = () => {
        tableBody.innerHTML = '';
        const records = db[DEPARTMENT_KEY] || [];
        countBadge.innerText = `${records.length} Kayıt`;

        if(records.length === 0){
            tableBody.innerHTML = `<tr><td colspan="4" class="px-6 py-12 text-center text-slate-400 font-medium">Bu departmanda henüz makine kaydı yok. Lütfen soldan ekleyin.</td></tr>`;
            return;
        }

        records.reverse().forEach((record, idx) => {
            // Index hesaplama ters çevrildiği için orijinal verinin indeksine erişim:
            const originalIndex = records.length - 1 - idx; 

            const tr = document.createElement('tr');
            tr.className = 'hover:bg-slate-50/50 transition-colors group';
            tr.innerHTML = `
                <td class="px-6 py-5 font-bold text-on-surface">${record.name}</td>
                <td class="px-6 py-5">
                    <span class="inline-flex items-center px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-tighter ${getStatusStyle(record.status)}">
                        ${record.status}
                    </span>
                </td>
                <td class="px-6 py-5">
                    <span class="text-lg font-black ${getPerfColor(record.performance)}">%${record.performance}</span>
                </td>
                <td class="px-6 py-5 text-right">
                    <button onclick="deleteRow(${originalIndex})" class="w-8 h-8 inline-flex items-center justify-center rounded-full bg-slate-100 text-slate-500 hover:bg-error hover:text-white transition-colors opacity-0 group-hover:opacity-100">
                        <span class="material-symbols-outlined text-sm">delete</span>
                    </button>
                </td>
            `;
            tableBody.appendChild(tr);
        });
    };

    // Global silme foksiyonu
    window.deleteRow = (index) => {
        db[DEPARTMENT_KEY].splice(index, 1);
        localStorage.setItem('productionDatabase', JSON.stringify(db));
        renderTable();
    };

    // Kayıt Ekle
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const name = document.getElementById('recordName').value;
        const status = document.getElementById('recordStatus').value;
        const performance = parseInt(document.getElementById('recordPerformance').value);

        if(!db[DEPARTMENT_KEY]){
            db[DEPARTMENT_KEY] = [];
        }

        db[DEPARTMENT_KEY].push({
            id: Date.now(),
            name: name,
            status: status,
            performance: performance
        });

        localStorage.setItem('productionDatabase', JSON.stringify(db));
        form.reset();
        renderTable();
    });

    // İlk yükleme
    renderTable();
});

