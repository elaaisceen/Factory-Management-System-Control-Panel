
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
    localStorage.setItem('systemLogs', JSON.stringify(logs.slice(0, 50)));
}

        document.addEventListener('DOMContentLoaded', () => {
            // Grafik için buton geçişleri (Haftalık / Aylık)
            const timeFilters = document.getElementById('timeFilters');
            if (timeFilters) {
                const buttons = timeFilters.querySelectorAll('button');
                buttons.forEach(btn => {
                    btn.addEventListener('click', () => {
                        // Sınıfları temizle
                        buttons.forEach(b => {
                            b.className = 'px-3 py-1 text-xs font-medium text-slate-400 rounded-md transition-all hover:bg-slate-100';
                        });
                        // Seçileni aktif yap
                        btn.className = 'px-3 py-1 bg-surface-container text-xs font-bold rounded-md transition-all text-on-surface';
                    });
                });
            }

            // Raporları İndir Butonu İşlevi (CSV İndirir)
            const exportReportBtn = document.getElementById('exportReportBtn');
            const downloadSpinner = document.getElementById('downloadSpinner');
            const downloadBtnText = document.getElementById('downloadBtnText');

            if (exportReportBtn) {
                exportReportBtn.addEventListener('click', () => {
                    exportReportBtn.disabled = true;
                    downloadSpinner.classList.remove('hidden');
                    downloadBtnText.innerText = "Hazırlanıyor...";

                    setTimeout(() => {
                        // Dosya oluşturma ve indirme işlemi
                        const csvData = [
                            ["Kaynak Adi", "Stok Kodu", "Miktar", "Durum"],
                            ["Yuksek Mukavemetli Aluminyum Bobin", "ALU-7822-M", "42", "Kritik Dusuk"],
                            ["Endustriyel Sogutucu Blue-X", "CLT-0041-L", "156", "Siparis Verilmeli"],
                            ["Hassas Celik Rulman L (12mm)", "BRG-9102-S", "2400", "Saglikli"]
                        ];

                        // BOM ekleyerek UTF-8 Excel uyumlu hale getiriyoruz
                        const csvContent = "data:text/csv;charset=utf-8,\uFEFF" + csvData.map(e => e.join(";")).join("\n");
                        const encodedUri = encodeURI(csvContent);
                        const link = document.createElement("a");
                        link.setAttribute("href", encodedUri);
                        link.setAttribute("download", "Uretim_Raporu.csv");
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);

                        // Görsel başarı durumu
                        downloadSpinner.classList.add('hidden');
                        downloadSpinner.classList.remove('animate-spin');
                        downloadSpinner.innerText = "check";
                        downloadSpinner.classList.remove('hidden');
                        downloadBtnText.innerText = "İndirildi";

                        setTimeout(() => {
                            exportReportBtn.disabled = false;
                            downloadSpinner.classList.add('hidden');
                            downloadSpinner.classList.add('animate-spin');
                            downloadSpinner.innerText = "hourglass_empty";
                            downloadBtnText.innerText = "Raporları İndir";
                        }, 2500);
                    }, 1500);
                });
            }

            // Protokol Başlat Butonu İşlevi (Enable / Disable Toggle)
            const initProtocolBtn = document.getElementById('initProtocolBtn');
            const protocolSpinner = document.getElementById('protocolSpinner');
            const protocolBtnText = document.getElementById('protocolBtnText');
            let isProtocolActive = false;

            if (initProtocolBtn) {
                initProtocolBtn.addEventListener('click', () => {
                    initProtocolBtn.disabled = true;
                    protocolSpinner.classList.remove('hidden');
                    protocolSpinner.classList.add('animate-spin');

                    if (!isProtocolActive) {
                        protocolBtnText.innerText = "Başlatılıyor...";
                        setTimeout(() => {
                            isProtocolActive = true;
                            protocolSpinner.classList.remove('animate-spin');
                            protocolSpinner.innerText = "stop_circle";
                            protocolBtnText.innerText = "Protokolü Durdur";
                            initProtocolBtn.classList.remove('from-primary', 'to-primary-container');
                            initProtocolBtn.classList.add('bg-error'); // Kırmızı
                            initProtocolBtn.disabled = false;
                        }, 1500);
                    } else {
                        protocolBtnText.innerText = "Durduruluyor...";
                        setTimeout(() => {
                            isProtocolActive = false;
                            protocolSpinner.classList.remove('animate-spin');
                            protocolSpinner.innerText = "sync";
                            protocolSpinner.classList.add('hidden');
                            protocolBtnText.innerText = "Protokol Başlat";
                            initProtocolBtn.classList.remove('bg-error');
                            initProtocolBtn.classList.add('from-primary', 'to-primary-container');
                            initProtocolBtn.disabled = false;
                        }, 1500);
                    }
                });
            }

            // FAB Yeni Satır Ekleme Sistemi
            const addInventoryFab = document.getElementById('addInventoryFab');
            const addEntryModal = document.getElementById('addEntryModal');
            const modalOverlay = document.getElementById('modalOverlay');
            const modalCloseBtn = document.getElementById('modalCloseBtn');
            const modalCloseIcon = document.getElementById('modalCloseIcon');
            const addEntryForm = document.getElementById('addEntryForm');
            const inventoryTableBody = document.getElementById('inventoryTableBody');

            if (addInventoryFab && addEntryModal) {
                const toggleModal = () => {
                    addEntryModal.classList.toggle('hidden');
                };

                addInventoryFab.addEventListener('click', toggleModal);
                modalOverlay.addEventListener('click', toggleModal);
                modalCloseBtn.addEventListener('click', toggleModal);
                modalCloseIcon.addEventListener('click', toggleModal);

                addEntryForm.addEventListener('submit', async (e) => {
                    e.preventDefault();

                    const name = document.getElementById('entryName').value;
                    const sku = document.getElementById('entrySku').value;
                    const qty = document.getElementById('entryQty').value;
                    const unit = document.getElementById('entryUnit').value;
                    const status = document.getElementById('entryStatus').value;

                    // Birim ve durumları API formatına dönüştür
                    const unitToDeptMap = {
                        'montaj': 2,
                        'kalite_kontrol': 2,
                        'planlama_tasarım': 1,
                        'paketleme': 2
                    };
                    
                    const statusToDurumMap = {
                        'aktif': 'DEVAM',
                        'bakım': 'PLANLANDI',
                        'pasif': 'IPTAL'
                    };

                    try {
                        // API'ye kaydet
                        const apiData = {
                            departmanId: unitToDeptMap[unit] || 2,
                            projeKodu: sku || name.substring(0, 20),
                            planTarihi: new Date().toISOString().split('T')[0],
                            durum: statusToDurumMap[status] || 'PLANLANDI'
                        };
                        
                        console.log('Üretim planı API verisi:', apiData);
                        
                        const response = await fetch('/api/uretim-plan', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(apiData)
                        });
                        
                        const result = await response.json();
                        console.log('API Yanıtı:', result);
                        
                        if (result.success) {
                            let statusBadge = '';
                            let errorFlag = '';

                            if (status === 'aktif') {
                                statusBadge = `<span class="inline-flex items-center gap-2 px-3 py-1 bg-secondary-container text-on-secondary-container rounded-full text-[10px] font-black uppercase tracking-tighter">Aktif Çalışıyor</span>`;
                                errorFlag = 'text-secondary';
                            } else if (status === 'bakım') {
                                statusBadge = `<span class="inline-flex items-center gap-2 px-3 py-1 bg-error-container text-on-error-container/70 rounded-full text-[10px] font-black uppercase tracking-tighter">Bakımda</span>`;
                                errorFlag = 'text-orange-500';
                            } else {
                                statusBadge = `<span class="inline-flex items-center gap-2 px-3 py-1 bg-error-container text-on-error-container rounded-full text-[10px] font-black uppercase tracking-tighter">Pasif</span>`;
                                errorFlag = 'text-error';
                            }

                            const newRow = document.createElement('tr');
                            newRow.className = "hover:bg-slate-50/50 transition-colors";
                            newRow.innerHTML = `
                        <td class="px-8 py-6 font-bold text-on-surface group relative">${name}<span class="absolute left-2 top-1/2 -translate-y-1/2 w-1.5 h-1.5 rounded-full bg-primary opacity-0 group-hover:opacity-100 transition-opacity"></span></td>
                        <td class="px-8 py-6 font-mono text-sm text-slate-400">${sku.toUpperCase()}</td>
                        <td class="px-8 py-6">
                            <span class="text-lg font-black ${errorFlag}">${Number(qty).toLocaleString('tr-TR')}</span>
                            <span class="text-xs font-medium text-slate-400 ml-1">${unit}</span>
                        </td>
                        <td class="px-8 py-6 text-right">${statusBadge}</td>
                    `;

                            inventoryTableBody.prepend(newRow);

                            // Log Event
                            logSystemEvent('Alex Sterling', `New Production Entry: ${name}`, 'Production Sector', 'SUCCESS');
                            
                            alert('Üretim planı veritabanına kaydedildi!');
                        } else {
                            alert('Hata: ' + result.message);
                        }

                        toggleModal();
                        addEntryForm.reset();
                    } catch (error) {
                        console.error('Üretim kaydetme hatası:', error);
                        alert('Kayıt hatası: ' + error.message);
                    }
                });
            }

            // API'den üretim planlarını yükle
            async function loadProductionPlans() {
                try {
                    const response = await fetch('/api/uretim-plan');
                    const result = await response.json();
                    
                    if (result.success && result.data) {
                        const totalSum = result.data.length;
                        const activeTotal = result.data.filter(p => p.durum === 'DEVAM').length;
                        const maintenanceTotal = result.data.filter(p => p.durum === 'PLANLANDI').length;
                        
                        const elActive = document.getElementById('dynActiveCount');
                        const elTotal = document.getElementById('dynTotalCount');
                        const elMaint = document.getElementById('dynMaintenanceText');

                        if (elActive) elActive.innerText = activeTotal;
                        if (elTotal) elTotal.innerText = totalSum;
                        if (elMaint) elMaint.innerText = maintenanceTotal > 0 ? `Şu anda ${maintenanceTotal} ünite bakımda/planlandı.` : 'Tüm sistemler çalışıyor.';
                        
                        console.log('Üretim planları API\'den yüklendi:', result.data);
                    }
                } catch (error) {
                    console.error('Üretim planları yüklenirken hata:', error);
                }
            }
            
            loadProductionPlans();
        });
