
        // Çıkış butonuna tıklandığında
        document.getElementById('confirmLogout').addEventListener('click', function() {
            performLogout();
        });

        // İptal butonuna tıklandığında
        document.getElementById('cancelLogout').addEventListener('click', function() {
            // Önceki sayfaya geri dön
            window.history.back();
        });

        function performLogout() {
            try {
                // LocalStorage'dan tüm verileri sil
                localStorage.clear();
                
                // SessionStorage'dan tüm verileri sil
                sessionStorage.clear();
                
                // Cookies'i sil
                deleteCookies();
                
                // Başarı mesajı göster
                showMessage('Oturumunuz başarıyla sonlandırıldı. Yeniden yönlendiriliyorsunuz...', 'success');
                
                // 2 saniye sonra login sayfasına yönlendir
                setTimeout(function() {
                    window.location.href = '../index.html';
                }, 2000);
                
            } catch (error) {
                console.error('Çıkış sırasında hata oluştu:', error);
                showMessage('Çıkış sırasında bir hata oluştu. Lütfen tekrar deneyiniz.', 'error');
            }
        }

        function deleteCookies() {
            // Tüm cookies'i sil
            const cookies = document.cookie.split(';');
            
            cookies.forEach(function(cookie) {
                const eqPos = cookie.indexOf('=');
                const name = eqPos > -1 ? cookie.substr(0, eqPos).trim() : cookie.trim();
                
                if (name) {
                    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
                }
            });
        }

        function showMessage(message, type) {
            const statusElement = document.getElementById('statusMessage');
            statusElement.textContent = message;
            statusElement.className = 'status-message ' + type;
            statusElement.style.display = 'block';
        }

        // Sayfa yüklendiğinde oturum kontrolü
        window.addEventListener('load', function() {
            // Opsiyonel: Eğer kullanıcı zaten oturum açmamışsa login sayfasına yönlendir
            const isLoggedIn = localStorage.getItem('isLoggedIn') || sessionStorage.getItem('isLoggedIn');
            
            if (!isLoggedIn) {
                // Kullanıcı oturum açmamışsa, login sayfasına yönlendir
                // window.location.href = '../login_register/loginRegister.html';
            }
        });
 
