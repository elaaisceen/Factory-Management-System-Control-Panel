/**
 * yeni_kayit.js — Kayıt Formu Mantığı + Pozisyon (Rol) Validasyonu
 *
 * ROL_METADATA_KAYIT haritası, Java Rol.java Enum ve
 * yeni_kayit.html <option value="N"> ile birebir eşleşir.
 */

// ─── Rol Metadata ─────────────────────────────────────────────────────────────
const ROL_METADATA_KAYIT = {
    1:  { adi: 'GENEL_MUDUR',        yetki: 'SUPER',    departman: 'YONETIM'   },
    2:  { adi: 'GELISTIRICI_ADMIN',  yetki: 'SUPER',    departman: 'IT'        },
    3:  { adi: 'BT_PERSONEL',        yetki: 'PERSONEL', departman: 'IT'        },
    4:  { adi: 'BT_YONETICI',        yetki: 'YONETICI', departman: 'IT'        },
    5:  { adi: 'IK_PERSONEL',        yetki: 'PERSONEL', departman: 'IK'        },
    6:  { adi: 'IK_YONETICI',        yetki: 'YONETICI', departman: 'IK'        },
    7:  { adi: 'DEPO_PERSONEL',      yetki: 'PERSONEL', departman: 'DEPO'      },
    8:  { adi: 'DEPO_YONETICI',      yetki: 'YONETICI', departman: 'DEPO'      },
    9:  { adi: 'URETIM_PERSONEL',    yetki: 'PERSONEL', departman: 'URETIM'    },
    10: { adi: 'URETIM_YONETICI',    yetki: 'YONETICI', departman: 'URETIM'    },
    11: { adi: 'FINANS_PERSONEL',    yetki: 'PERSONEL', departman: 'FINANS'    },
    12: { adi: 'FINANS_YONETICI',    yetki: 'YONETICI', departman: 'FINANS'    },
    13: { adi: 'SATINALMA_PERSONEL', yetki: 'PERSONEL', departman: 'SATINALMA' },
    14: { adi: 'SATINALMA_YONETICI', yetki: 'YONETICI', departman: 'SATINALMA' },
};

// ─── CV Dosya Seçimi ──────────────────────────────────────────────────────────
document.getElementById('cvFile').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const fileNameElement = document.getElementById('fileName');

    if (file) {
        const maxSize = 5 * 1024 * 1024; // 5 MB
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

// ─── Yardımcı Validatörler ────────────────────────────────────────────────────
function validatePhoneNumber(phone) {
    // Türkiye telefon: 05XX XXX XX XX
    const phoneRegex = /^(\+90|0)?[5][0-9]{2}[0-9]{3}[0-9]{4}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

// ─── Form Submit ──────────────────────────────────────────────────────────────
document.querySelector('form').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    let isValid = true;
    let errorMessage = '';

    // Ad
    if (!formData.get('firstName')?.trim()) {
        isValid = false; errorMessage = 'Ad alanı zorunludur.';
    }
    // Soyad
    if (isValid && !formData.get('lastName')?.trim()) {
        isValid = false; errorMessage = 'Soyad alanı zorunludur.';
    }
    // E-posta
    const email = formData.get('email')?.trim();
    if (isValid && !email) {
        isValid = false; errorMessage = 'E-posta alanı zorunludur.';
    } else if (isValid && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        isValid = false; errorMessage = 'Geçerli bir e-posta adresi giriniz.';
    }
    // Telefon
    const phone = formData.get('phone')?.trim();
    if (isValid && !phone) {
        isValid = false; errorMessage = 'Telefon numarası zorunludur.';
    } else if (isValid && !validatePhoneNumber(phone)) {
        isValid = false; errorMessage = 'Geçerli bir telefon numarası giriniz (05XX XXX XX XX).';
    }
    // Mezuniyet
    if (isValid && !formData.get('education')) {
        isValid = false; errorMessage = 'Mezuniyet bilgisi seçiniz.';
    }
    // ── Pozisyon (Rol) Validasyonu ────────────────────────────────────────────
    // 2. <select> = pozisyon kutusu (1. <select> = mezuniyet)
    const pozisyonSelect = document.querySelectorAll('select')[1];
    const pozisyonDegeri = pozisyonSelect ? parseInt(pozisyonSelect.value) : NaN;
    if (isValid && (!pozisyonDegeri || !ROL_METADATA_KAYIT[pozisyonDegeri])) {
        isValid = false; errorMessage = 'Firmamızdaki pozisyonunuzu seçiniz.';
    }
    // CV
    const cvFile = document.getElementById('cvFile').files[0];
    if (isValid && !cvFile) {
        isValid = false; errorMessage = 'CV dosyası zorunludur.';
    }
    // Şifre
    const password        = formData.get('password');
    const confirmPassword = formData.get('confirmPassword');
    if (isValid && (!password || password.length < 6)) {
        isValid = false; errorMessage = 'Şifre en az 6 karakter olmalıdır.';
    } else if (isValid && password !== confirmPassword) {
        isValid = false; errorMessage = 'Şifreler eşleşmiyor.';
    }
    // Sözleşme
    if (isValid && !document.querySelector('input[type="checkbox"]').checked) {
        isValid = false; errorMessage = 'Kullanım koşullarını kabul etmeniz gerekir.';
    }

    if (!isValid) { alert(errorMessage); return; }

    // ── Başarılı Kayıt ────────────────────────────────────────────────────────
    const submitButton = document.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.textContent = 'Kayıt yapılıyor...';
    submitButton.disabled = true;

    // Gerçek uygulamada: fetch('/api/auth/kayit', { method: 'POST', body: formData })
    //   .then(r => r.json()).then(data => { localStorage.setItem('oturum', ...); ... });

    // Demo: seçilen pozisyon bilgisini oturum olarak kaydet (şifre yazılmaz!)
    const ad  = (formData.get('firstName') + ' ' + formData.get('lastName')).trim();
    const rol = ROL_METADATA_KAYIT[pozisyonDegeri];
    const oturum = {
        kullaniciId:  Date.now(), // Demo; gerçekte API'den gelir
        kullaniciAdi: ad,
        rolId:        pozisyonDegeri,
        rolAdi:       rol.adi,
        yetki:        rol.yetki,
        departman:    rol.departman,
        girisZamani:  new Date().toISOString(),
    };
    localStorage.setItem('oturum', JSON.stringify(oturum));

    setTimeout(() => {
        alert('Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz.');
        submitButton.textContent = originalText;
        submitButton.disabled    = false;
        window.location.href = 'loginRegister.html';
    }, 2000);
});

// ─── Input name Atamaları ─────────────────────────────────────────────────────
document.querySelector('input[placeholder="Mehmet"]').name = 'firstName';
document.querySelector('input[placeholder="Yılmaz"]').name = 'lastName';
document.querySelector('input[placeholder="mehmet.yilmaz@fabrika.com"]').name = 'email';
document.querySelector('input[placeholder="0555 123 45 67"]').name = 'phone';
document.querySelector('select').name = 'education';

const passwordInputs = document.querySelectorAll('input[placeholder="••••••••"]');
if (passwordInputs[0]) passwordInputs[0].name = 'password';
if (passwordInputs[1]) passwordInputs[1].name = 'confirmPassword';
