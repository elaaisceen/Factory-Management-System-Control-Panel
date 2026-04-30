
    let currentNetSalary = 0;
    
    function openModal(name, dept, base, days, overtime, status, net) {
        document.getElementById('payrollModal').classList.remove('hidden');
        
        document.getElementById('modalEmployeeName').innerText = `${name} | ${dept}`;
        document.getElementById('modalBaseSalary').innerText = formatCurrency(base);
        document.getElementById('modalDaysWorked').innerText = `${days} Gün`;
        document.getElementById('modalOvertime').innerText = `${overtime} Saat`;
        
        currentNetSalary = net;
        updateNetSalaryUI();

        // Populate basic list
        const list = document.getElementById('payrollItemsList');
        list.innerHTML = `
            <li class="flex justify-between p-3 bg-white border border-slate-100 rounded-xl shadow-sm">
                <span class="text-sm font-medium text-slate-600">Baz Maaş</span>
                <span class="text-sm font-bold text-slate-800">${formatCurrency(base)}</span>
            </li>
        `;
        
        if (overtime > 0) {
            const overtimePay = overtime * 250; // Örnek saatlik mesai ücreti
            list.innerHTML += `
                <li class="flex justify-between p-3 bg-green-50 border border-green-100 rounded-xl shadow-sm">
                    <span class="text-sm font-medium text-green-700">Mesai Ücreti (${overtime} Saat)</span>
                    <span class="text-sm font-bold text-green-700">+${formatCurrency(overtimePay)}</span>
                </li>
            `;
        }
        
    }

    function closeModal() {
        document.getElementById('payrollModal').classList.add('hidden');
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('tr-TR', { style: 'currency', currency: 'TRY' }).format(amount);
    }

    function updateNetSalaryUI() {
        document.getElementById('modalNetSalary').innerText = formatCurrency(currentNetSalary);
    }

    function addBenefitRecord(type) {
        const select = document.getElementById('benefitType');
        const amountInput = document.getElementById('benefitAmount');
        const amount = parseFloat(amountInput.value);
        
        if(isNaN(amount) || amount <= 0) {
            alert('Lütfen geçerli bir tutar giriniz!');
            return;
        }

        const typeName = select.options[select.selectedIndex].text;
        const list = document.getElementById('payrollItemsList');
        
        const isAdd = type === 'add';
        const colorClass = isAdd ? 'text-green-700 bg-green-50 border-green-100' : 'text-red-700 bg-red-50 border-red-100';
        const sign = isAdd ? '+' : '-';
        
        list.innerHTML += `
            <li class="flex justify-between p-3 border rounded-xl shadow-sm ${colorClass}">
                <span class="text-sm font-medium">${typeName}</span>
                <span class="text-sm font-bold">${sign}${formatCurrency(amount)}</span>
            </li>
        `;
        
        if(isAdd) currentNetSalary += amount;
        else currentNetSalary -= amount;
         async function saveBordro() {
        const payload = {
            personelAd: document.getElementById('modalEmployeeName').innerText.split('|')[0].trim(),
            netOdenen: currentNetSalary,
            donemAyYil: "04-2026",
            bazMaas: parseFloat(document.getElementById('modalBaseSalary').innerText.replace(/[^0-9,-]/g, '').replace(',', '.')),
            calisilanGun: parseInt(document.getElementById('modalDaysWorked').innerText),
            mesaiSaati: parseInt(document.getElementById('modalOvertime').innerText),
            durum: "Yatırıldı"
        };

        const onaylaBtn = document.querySelector('button[onclick="saveBordro()"]');
        const originalText = onaylaBtn.innerHTML;
        
        try {
            onaylaBtn.innerHTML = '<span class="material-symbols-outlined text-base animate-spin">refresh</span> Kaydediliyor...';
            onaylaBtn.disabled = true;

            const response = await fetch('http://localhost:8080/api/bordro/kaydet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (result.success) {
                alert("Başarılı: " + result.message);
                closeModal();
                // Opsiyonel: Tablodaki durumu güncelle
            } else {
                alert("Hata: " + result.message);
            }

        } catch(error) {
            console.error("Hata:", error);
            alert("Ağ hatası veya Sunucuya ulaşılamıyor! Lütfen backend servisinin çalıştığından emin olun.");
        } finally {
            onaylaBtn.innerHTML = originalText;
            onaylaBtn.disabled = false;
        }
    }
}
