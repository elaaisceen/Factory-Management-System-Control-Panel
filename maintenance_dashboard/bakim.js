 document.addEventListener('DOMContentLoaded', () => {
            const DEPARTMENT_KEY = 'bakim';

            // Veritabanı yapısını yükle veya oluştur (Bakım tablosu yoksa entegre et)
            let db = JSON.parse(localStorage.getItem('productionDatabase')) || {
                montaj: [], paketleme: [], kalite_kontrol: [], planlama_tasarim: [], bakim: []
            };

            if (!db.bakim) {
                db.bakim = [];
            }

            const tableBody = document.getElementById('dataTableBody');
            const form = document.getElementById('deptForm');
            const countBadge = document.getElementById('recordCount');
            const statOpen = document.getElementById('statOpen');
            const statClosed = document.getElementById('statClosed');

            // Renk Çözücüler Bakım İçin
            const getStatusStyle = (status) => {
                if (status === 'Onarım Bekliyor') return 'bg-error text-white shadow-sm shadow-error/30 animate-pulse';
                if (status === 'Devam Ediyor') return 'bg-orange-500 text-white';
                if (status === 'Çözüldü') return 'bg-secondary-container text-on-secondary-container';
                return 'bg-slate-100 text-slate-800';
            };

            const getPriorityStyle = (pri) => {
                if (pri === 'Acil') return 'text-error font-black';
                if (pri === 'Normal') return 'text-on-surface font-bold';
                if (pri === 'Düşük') return 'text-slate-400 font-medium';
                return 'text-slate-800';
            };

            const getPriorityIcon = (pri) => {
                if (pri === 'Acil') return '<span class="material-symbols-outlined text-[14px] text-error ml-1">priority_high</span>';
                if (pri === 'Düşük') return '<span class="material-symbols-outlined text-[14px] text-slate-300 ml-1">arrow_downward</span>';
                return '';
            };

            // Tabloyu Çizdir
            const renderTable = () => {
                tableBody.innerHTML = '';
                const records = db.bakim || [];
                countBadge.innerText = `${records.length} Kayıt`;

                let openCount = 0;
                let closedCount = 0;

                if (records.length === 0) {
                    tableBody.innerHTML = `<tr><td colspan="6" class="px-6 py-12 text-center text-slate-400 font-medium flex-col items-center justify-center"><span class="material-symbols-outlined text-4xl mb-2 opacity-50 block text-center w-full">build_circle</span>Aktif veya geçmiş bir arıza kaydı bulunmuyor.</td></tr>`;
                    statOpen.innerText = "0";
                    statClosed.innerText = "0";
                    return;
                }

                records.reverse().forEach((record, idx) => {
                    const originalIndex = records.length - 1 - idx;

                    if (record.status === 'Çözüldü') closedCount++;
                    else openCount++;

                    const tr = document.createElement('tr');
                    tr.className = `transition-colors group ${record.status === 'Çözüldü' ? 'hover:bg-slate-50/50 bg-slate-50/20 opacity-70' : 'hover:bg-orange-50/50'}`;
                    tr.innerHTML = `
                <td class="px-6 py-5 font-bold text-on-surface">${record.name}</td>
                <td class="px-6 py-5">
                    <div class="font-bold text-slate-800 text-sm whitespace-nowrap">${record.department || 'Bilinmiyor'}</div>
                    ${['Montaj', 'Paketleme', 'Planlama / Tasarım'].includes(record.department) ? '<div class="text-[10px] text-error font-black uppercase mt-1 flex items-center gap-1"><span class="w-1.5 h-1.5 rounded-full bg-error animate-pulse"></span>Downtime Riski!</div>' : ''}
                </td>
                <td class="px-6 py-5 text-sm font-medium text-slate-600 max-w-xs truncate">${record.issue}</td>
                <td class="px-6 py-5">
                    <div class="flex items-center">
                        <span class="${getPriorityStyle(record.priority)}">${record.priority}</span>
                        ${getPriorityIcon(record.priority)}
                    </div>
                </td>
                <td class="px-6 py-5">
                    <span class="inline-flex items-center px-3 py-1.5 rounded-full text-[10px] font-black uppercase tracking-tighter ${getStatusStyle(record.status)}">
                        ${record.status}
                    </span>
                </td>
                <td class="px-6 py-5 text-right">
                    <button onclick="deleteRow(${originalIndex})" class="w-8 h-8 inline-flex items-center justify-center rounded-full bg-slate-100 text-slate-500 hover:bg-error hover:text-white transition-colors opacity-0 group-hover:opacity-100">
                        <span class="material-symbols-outlined text-sm">delete</span>
                    </button>
                </td>
            `;
                    tableBody.appendChild(tr);
                });

                statOpen.innerText = openCount;
                statClosed.innerText = closedCount;
            };

            // Global silme
            window.deleteRow = (index) => {
                db[DEPARTMENT_KEY].splice(index, 1);
                localStorage.setItem('productionDatabase', JSON.stringify(db));
                renderTable();
            };

            // Kayıt Ekle
            form.addEventListener('submit', (e) => {
                e.preventDefault();

                const name = document.getElementById('recordName').value;
                const issue = document.getElementById('recordIssue').value;
                const priority = document.getElementById('recordPriority').value;
                const status = document.getElementById('recordStatus').value;
                const departmentBase = document.getElementById('recordDepartment').value;

                let departmentLiteral = departmentBase;
                if (departmentBase.includes('Montaj')) departmentLiteral = 'Montaj';
                else if (departmentBase.includes('Paketleme')) departmentLiteral = 'Paketleme';
                else if (departmentBase.includes('Planlama / Tasarım')) departmentLiteral = 'Planlama / Tasarım';
                else if (departmentBase.includes('Kalite')) departmentLiteral = 'Kalite Kontrol';

                db[DEPARTMENT_KEY].push({
                    id: Date.now(),
                    name: name,
                    department: departmentLiteral,
                    issue: issue,
                    priority: priority,
                    status: status
                });

                localStorage.setItem('productionDatabase', JSON.stringify(db));
                form.reset();

                // Form animasyonu
                const btn = form.querySelector('button[type="submit"]');
                const oldText = btn.innerText;
                const oldClass = btn.className;
                btn.innerText = "Eklendi!";
                btn.className = "w-full py-3 mt-4 bg-secondary text-white font-bold rounded-xl shadow-md transition-all scale-105";

                setTimeout(() => {
                    btn.innerText = oldText;
                    btn.className = oldClass;
                }, 1500);

                renderTable();
            });

            // İlk yükleme
            renderTable();
        });

