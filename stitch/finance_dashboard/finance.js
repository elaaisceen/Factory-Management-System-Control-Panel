//Veriyi Dışarı Çıkart JS
    
        document.addEventListener('DOMContentLoaded', function () {
            const btn = document.querySelector('#exportBtn');
            const menu = document.querySelector('#exportMenu');
            const printBtn = document.querySelector('#printReport');
            const pdfBtn = document.querySelector('#exportPDF');

            // 1. Butona basınca menüyü aç/kapat
            btn.addEventListener('click', function (e) {
                e.stopPropagation(); // Tıklamanın dışarı taşmasını engelle
                menu.classList.toggle('hidden');
            });

            // 2. Sayfada başka bir yere basınca menüyü kapat
            document.addEventListener('click', function () {
                menu.classList.add('hidden');
            });

            // 3. Yazdır veya PDF'e basınca işlemi yap
            [printBtn, pdfBtn].forEach(el => {
                el.addEventListener('click', function () {
                    window.print();
                });
            });

            // Excel için (Şimdilik sadece uyarı verir)
            document.querySelector('#exportExcel').addEventListener('click', function () {
                alert('Excel verisi hazırlanıyor...');
            });
        });
    

    //Veri JS
    
        document.addEventListener('DOMContentLoaded', function () {
            const newDataBtn = document.getElementById('newDataBtn');
            const newDataMenu = document.getElementById('newDataMenu');

            // Butona tıklandığında menüyü aç/kapat
            newDataBtn.addEventListener('click', function (e) {
                e.stopPropagation(); // Tıklamanın dışarı sızmasını engelle
                newDataMenu.classList.toggle('hidden');
            });

            // Menü dışındaki bir yere tıklandığında menüyü kapat
            document.addEventListener('click', function () {
                newDataMenu.classList.add('hidden');
            });
        });


        function handleLogout() {
            const onay = confirm("Oturumunuzu sonlandırmak istediğinize emin misiniz?\\nTüm oturum verileri silinecektir.");
            if (onay) {
                try {
                    localStorage.clear();
                    sessionStorage.clear();
                    const cookies = document.cookie.split(';');
                    cookies.forEach(function (cookie) {
                        const eqPos = cookie.indexOf('=');
                        const name = eqPos > -1 ? cookie.substr(0, eqPos).trim() : cookie.trim();
                        if (name) {
                            document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
                        }
                    });
                    alert("Oturum başarıyla sonlandırıldı. Giriş sayfasına yönlendiriliyorsunuz...");
                    window.location.href = '../../index.html';
                } catch (error) {
                    alert("Çıkış yapılırken bir hata oluştu!");
                    console.error(error);
                }
            }
        }
