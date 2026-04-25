// CV dosya seçildiğinde dosya adını göster
    document.getElementById('cvFile').addEventListener('change', function(e) {
        const file = e.target.files[0];
        const fileNameElement = document.getElementById('fileName');
        
        if (file) {
            // Dosya boyutu kontrolü (5MB = 5 * 1024 * 1024 bytes)
            const maxSize = 5 * 1024 * 1024;
            if (file.size > maxSize) {
                alert('Dosya boyutu 5MB\'dan büyük olamaz!');
                e.target.value = '';
                fileNameElement.textContent = 'CV dosyanızı seçin (PDF, DOC, DOCX)';
                fileNameElement.className = 'text-outline-variant';
                return;
            }
            
            fileNameElement.textContent = file.name;
            fileNameElement.className = 'text-on-surface font-medium';
        } else {
            fileNameElement.textContent = 'CV dosyanızı seçin (PDF, DOC, DOCX)';
            fileNameElement.className = 'text-outline-variant';
        }
    });

    // Form validasyon fonksiyonu
    function validatePhoneNumber(phone) {
        // Türkiye telefon formatı: 05XX XXX XX XX veya 05XXXXXXXXX
        const phoneRegex = /^(\+90|0)?[5][0-9]{2}[0-9]{3}[0-9]{4}$/;
        return phoneRegex.test(phone.replace(/\s/g, ''));
    }

    // Form submit olayını yakala
    document.querySelector('form').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        let isValid = true;
        let errorMessage = '';

        // Ad kontrolü
        if (!formData.get('firstName')?.trim()) {
            isValid = false;
            errorMessage = 'Ad alanı zorunludur.';
        }

        // Soyad kontrolü
        if (!formData.get('lastName')?.trim()) {
            isValid = false;
            errorMessage = 'Soyad alanı zorunludur.';
        }

        // E-posta kontrolü
        const email = formData.get('email')?.trim();
        if (!email) {
            isValid = false;
            errorMessage = 'E-posta alanı zorunludur.';
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            isValid = false;
            errorMessage = 'Geçerli bir e-posta adresi giriniz.';
        }

        // Telefon kontrolü
        const phone = formData.get('phone')?.trim();
        if (!phone) {
            isValid = false;
            errorMessage = 'Telefon numarası zorunludur.';
        } else if (!validatePhoneNumber(phone)) {
            isValid = false;
            errorMessage = 'Geçerli bir telefon numarası giriniz (05XX XXX XX XX).';
        }

        // Mezuniyet kontrolü
        if (!formData.get('education')) {
            isValid = false;
            errorMessage = 'Mezuniyet bilgisi seçiniz.';
        }

        // CV kontrolü
        const cvFile = document.getElementById('cvFile').files[0];
        if (!cvFile) {
            isValid = false;
            errorMessage = 'CV dosyası zorunludur.';
        }

        // Şifre kontrolü
        const password = formData.get('password');
        const confirmPassword = formData.get('confirmPassword');
        if (!password || password.length < 6) {
            isValid = false;
            errorMessage = 'Şifre en az 6 karakter olmalıdır.';
        } else if (password !== confirmPassword) {
            isValid = false;
            errorMessage = 'Şifreler eşleşmiyor.';
        }

        // Sözleşme kontrolü
        if (!document.querySelector('input[type="checkbox"]').checked) {
            isValid = false;
            errorMessage = 'Kullanım koşullarını kabul etmeniz gerekir.';
        }

        if (!isValid) {
            alert(errorMessage);
            return;
        }

        // Başarılı kayıt simülasyonu
        const submitButton = document.querySelector('button[type="submit"]');
        const originalText = submitButton.textContent;
        submitButton.textContent = 'Kayıt yapılıyor...';
        submitButton.disabled = true;

        // Gerçek uygulamada burada AJAX çağrısı yapılır
        setTimeout(() => {
            alert('Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz.');
            window.location.href = 'loginRegister.php';
        }, 2000);
    });

    // Input name'lerini ekle (form validasyonu için gerekli)
    document.querySelector('input[placeholder="Mehmet"]').name = 'firstName';
    document.querySelector('input[placeholder="Yılmaz"]').name = 'lastName';
    document.querySelector('input[placeholder="mehmet.yilmaz@fabrika.com"]').name = 'email';
    document.querySelector('input[placeholder="0555 123 45 67"]').name = 'phone';
    document.querySelector('select').name = 'education';
    document.querySelector('input[placeholder="••••••••"]:nth-of-type(1)').name = 'password';
    document.querySelector('input[placeholder="••••••••"]:nth-of-type(2)').name = 'confirmPassword';
