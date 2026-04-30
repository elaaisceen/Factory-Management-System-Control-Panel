/**
 * yeni_kayit.js — Kayıt Formu Mantığı (localStorage tabanlı)
 */

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


// ─── UI Yardımcıları ──────────────────────────────────────────────────────────
function showError(msg) {
    const banner = document.getElementById('errorBanner');
    const text   = document.getElementById('errorText');
    if (banner && text) {
        text.textContent = msg;
        banner.classList.remove('hidden');
        banner.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
}
function hideError() {
    const banner = document.getElementById('errorBanner');
    if (banner) banner.classList.add('hidden');
}
function showSuccess(msg) {
    const banner = document.getElementById('successBanner');
    if (banner) { banner.textContent = msg; banner.classList.remove('hidden'); }
}
function setButtonLoading(loading) {
    const btn = document.getElementById('registerBtn');
    if (!btn) return;
    btn.disabled = loading;
    btn.textContent = loading ? 'Kaydediliyor...' : 'Kayıt Ol';
}

// ─── Validasyon ───────────────────────────────────────────────────────────────
function validatePhone(phone) {
    return /^(\+90|0)?[5][0-9]{9}$/.test(phone.replace(/\s/g,''));
}
function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// ─── Kullanıcı Kaydet (localStorage) ─────────────────────────────────────────
function kullaniciKaydet(kullanici) {
    const mevcut = JSON.parse(localStorage.getItem('kayitliKullanicilar') || '[]');
    
    // E-posta veya telefon zaten kayıtlı mı?
    const emailVarMi  = mevcut.some(k => k.email   === kullanici.email);
    const telefonVarMi = mevcut.some(k => k.telefon === kullanici.telefon);
    if (emailVarMi)  { return { basarili: false, mesaj: 'Bu e-posta adresi zaten kayıtlı.' }; }
    if (telefonVarMi) { return { basarili: false, mesaj: 'Bu telefon numarası zaten kayıtlı.' }; }

    kullanici.id = Date.now();
    kullanici.kayitTarihi = new Date().toISOString();
    mevcut.push(kullanici);
    localStorage.setItem('kayitliKullanicilar', JSON.stringify(mevcut));
    return { basarili: true };
}

// ─── Form Submit ──────────────────────────────────────────────────────────────
document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    hideError();

    const ad       = document.getElementById('firstName').value.trim();
    const soyad    = document.getElementById('lastName').value.trim();
    const email    = document.getElementById('email').value.trim();
    const telefon  = document.getElementById('telefon').value.trim();
    const mezuniyet = document.getElementById('mezuniyet').value;
    const pozisyon = parseInt(document.getElementById('pozisyon').value);
    const sifre    = document.getElementById('sifre').value;
    const sifreOnay = document.getElementById('sifreOnay').value;
    const sozlesme = document.getElementById('sozlesme').checked;

    // Validasyon
    if (!ad)                          { showError('Ad alanı zorunludur.'); return; }
    if (!soyad)                       { showError('Soyad alanı zorunludur.'); return; }
    if (!email)                       { showError('E-posta alanı zorunludur.'); return; }
    if (!validateEmail(email))        { showError('Geçerli bir e-posta adresi giriniz.'); return; }
    if (!telefon)                     { showError('Telefon numarası zorunludur.'); return; }
    if (!validatePhone(telefon))      { showError('Geçerli bir telefon numarası giriniz (05XX XXX XX XX).'); return; }
    if (!mezuniyet)                   { showError('Mezuniyet bilgisi seçiniz.'); return; }
    if (!pozisyon || !ROL_METADATA_KAYIT[pozisyon]) { showError('Firmamızdaki pozisyonunuzu seçiniz.'); return; }
    // if (!cvDosya)                     { showError('CV dosyası zorunludur.'); return; } // CV ARTIK OPSİYONEL
    if (!sifre || sifre.length < 6)   { showError('Şifre en az 6 karakter olmalıdır.'); return; }
    if (sifre !== sifreOnay)          { showError('Şifreler eşleşmiyor.'); return; }
    if (!sozlesme)                    { showError('Kullanım koşullarını kabul etmeniz gerekir.'); return; }

    setButtonLoading(true);

    fetch('http://localhost:8080/api/auth/kayit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
            ad, 
            soyad, 
            email, 
            telefon, 
            rolId: parseInt(pozisyon), // Sayıya çevirerek gönder
            sifre 
        }),
        signal: AbortSignal.timeout(10000)
    })
    .then(r => r.json())
    .then(data => {
        if (data.success) {
            showSuccess('Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz...');
            setTimeout(() => window.location.href = 'loginRegister.html', 2000);
        } else {
            showError(data.message || 'Kayıt başarısız. Lütfen tekrar deneyin.');
            setButtonLoading(false);
        }
    })
    .catch(() => {
        // Backend yoksa localStorage'a kaydet
        const rol = ROL_METADATA_KAYIT[pozisyon];
        const yeniKullanici = {
            ad, soyad, email,
            telefon: telefon.replace(/\s/g,''),
            sifre,
            rolId: pozisyon,
            rolAdi: rol.adi,
            yetki: rol.yetki,
            departman: rol.departman,
            mezuniyet,
            cinsiyet: document.getElementById('cinsiyet').value,
            askerlik: document.getElementById('askerlik').value,
        };

        const sonuc = kullaniciKaydet(yeniKullanici);
        if (sonuc.basarili) {
            showSuccess('✓ Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz...');
            setTimeout(() => window.location.href = 'loginRegister.html', 2000);
        } else {
            showError(sonuc.mesaj);
            setButtonLoading(false);
        }
    });
});
