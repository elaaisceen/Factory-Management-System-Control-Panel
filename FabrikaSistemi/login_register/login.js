
    // E-posta veya telefon validasyon fonksiyonu
    function validateEmailOrPhone(input) {
        const value = input.trim();
        
        // E-posta regex
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        // Telefon regex (Türkiye formatı: 05XX XXX XX XX veya 05XXXXXXXXX)
        const phoneRegex = /^(\+90|0)?[5][0-9]{2}[0-9]{3}[0-9]{4}$/;
        
        return emailRegex.test(value) || phoneRegex.test(value);
    }

    // Form submit olayını yakala
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const input = document.getElementById('loginInput');
        const errorDiv = document.getElementById('inputError');
        const value = input.value.trim();
        
        if (!value) {
            showError('Bu alan zorunludur.');
            input.focus();
            return;
        }
        
        if (!validateEmailOrPhone(value)) {
            showError('Geçerli bir e-posta adresi veya telefon numarası giriniz.');
            input.focus();
            return;
        }
        /*if (value !== dogruEposta || passwordValue !== dogruSifre) {
            showError('E-posta adresi veya şifre hatalı!'); // Hata mesajını göster
            return; // Fonksiyonu burada durdur, aşağıya geçme
        }*/

        
        // Validasyon başarılı, formu gönder
        hideError();
        
        // Burada normal login işlemi yapılır
        // Şimdilik sadece console'a yazdırıyorum
        console.log('Giriş yapılıyor:', value);
        
        // Demo amaçlı başarılı giriş simülasyonu
        showSuccessMessage('Giriş yapılıyor...');
        
        // Gerçek uygulamada burada AJAX çağrısı yapılır
        setTimeout(() => {
            // Başarılı giriş sonrası yönlendirme
            window.location.href = './admin_dashboard/adminDashboard.html';
            alert('Giriş başarılı! Yönlendiriliyorsunuz...');
        }, 1000); 
    }); 

   

    function showError(message) {
        const errorDiv = document.getElementById('inputError');
        errorDiv.textContent = message;
        errorDiv.classList.remove('hidden');
        
        // Input border rengini kırmızı yap
        const inputContainer = document.getElementById('loginInput').parentElement;
        inputContainer.classList.add('border-error/50');
    }

    function hideError() {
        const errorDiv = document.getElementById('inputError');
        errorDiv.classList.add('hidden');
        
        // Input border rengini normale döndür
        const inputContainer = document.getElementById('loginInput').parentElement;
        inputContainer.classList.remove('border-error/50');
    }

    function showSuccessMessage(message) {
        // Geçici başarı mesajı için
        const button = document.querySelector('button[type="submit"]');
        const originalText = button.textContent;
        button.textContent = message;
        button.disabled = true;
        
        setTimeout(() => {
            button.textContent = originalText;
            button.disabled = false;
        }, 2000);
    }

    // Input'a odaklandığında hatayı gizle
    document.getElementById('loginInput').addEventListener('focus', function() {
        hideError();
    });

    // Enter tuşuna basıldığında form submit et
    document.getElementById('loginInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            document.getElementById('loginForm').dispatchEvent(new Event('submit'));
        }
    });
