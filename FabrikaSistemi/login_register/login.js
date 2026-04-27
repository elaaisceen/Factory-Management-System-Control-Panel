/**
 * login.js — Giriş Formu Mantığı + Rol Tabanlı Yönlendirme
 *
 * Rol → Dashboard eşlemesi:
 *   SUPER (1,2)           → adminDashboard.html
 *   IT    (3,4)           → it_dashboard/it.html
 *   IK    (5,6)           → hr_dashboard/hr.html
 *   Depo  (7,8)           → stock_dashboard/stock.html
 *   Üretim(9,10)          → production_dashboard/production.html
 *   Finans(11,12)         → finance_dashboard/finance.html
 *   Satın Alma (13,14)    → purchasing_dashboard/purchasing.html
 *
 * UI Gizleme: Backend'den gelen rol bilgisi localStorage'a yazılır;
 * her dashboard yüklendiğinde menü öğeleri rol kontrolüne göre gizlenir.
 */

// ─── Rol → Dashboard Yönlendirme Haritası ────────────────────────────────────

const ROL_YONLENDIRME = {
    1:  '../login_register/admin_dashboard/adminDashboard.html',  // Genel Müdür
    2:  '../login_register/admin_dashboard/adminDashboard.html',  // Geliştirici/Admin
    3:  '../it_dashboard/it.html',                                // BT Personeli
    4:  '../it_dashboard/it.html',                                // BT Yöneticisi
    5:  '../hr_dashboard/hr.html',                                // IK Personeli
    6:  '../hr_dashboard/hr.html',                                // IK Yöneticisi
    7:  '../stock_dashboard/stock.html',                          // Depo Personeli
    8:  '../stock_dashboard/stock.html',                          // Depo Yöneticisi
    9:  '../production_dashboard/production.html',                // Üretim Personeli
    10: '../production_dashboard/production.html',                // Üretim Yöneticisi
    11: '../finance_dashboard/finance.html',                      // Finans Personeli
    12: '../finance_dashboard/finance.html',                      // Finans Yöneticisi
    13: '../purchasing_dashboard/purchasing.html',                 // Satın Alma Personeli
    14: '../purchasing_dashboard/purchasing.html',                 // Satın Alma Yöneticisi
};

// ─── Rol Metadata (Enum ile uyumlu) ──────────────────────────────────────────

const ROL_METADATA = {
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

// ─── Oturum Yönetimi ─────────────────────────────────────────────────────────

/**
 * Başarılı giriş sonrasında kullanıcı bilgilerini localStorage'a yazar.
 * ŞİFRE ASLA buraya yazılmaz.
 */
function oturumuKaydet(kullaniciId, kullaniciAdi, rolId) {
    const rol = ROL_METADATA[rolId];
    if (!rol) return;

    const oturum = {
        kullaniciId,
        kullaniciAdi,
        rolId,
        rolAdi:    rol.adi,
        yetki:     rol.yetki,
        departman: rol.departman,
        girisZamani: new Date().toISOString(),
    };
    localStorage.setItem('oturum', JSON.stringify(oturum));
}

/**
 * Mevcut oturum verisini döndürür.
 * Her dashboard başında çağrılarak menü gizleme kararı verilir.
 */
function oturumuAl() {
    try {
        return JSON.parse(localStorage.getItem('oturum'));
    } catch {
        return null;
    }
}

/** Oturumu temizler (Çıkış Yap). */
function oturumuKapat() {
    localStorage.removeItem('oturum');
    window.location.href = '../login_register/loginRegister.html';
}

// ─── Validasyon Yardımcıları ─────────────────────────────────────────────────

function validateEmailOrPhone(input) {
    const value = input.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^(\+90|0)?[5][0-9]{2}[0-9]{3}[0-9]{4}$/;
    return emailRegex.test(value) || phoneRegex.test(value);
}

function validatePassword(password) {
    return password && password.trim().length >= 6;
}

// ─── UI Hata Gösterimi ────────────────────────────────────────────────────────

function showError(message, fieldId) {
    const errorDiv = document.getElementById('inputError');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.remove('hidden');
    }
    if (fieldId) {
        const field = document.getElementById(fieldId);
        if (field && field.parentElement) {
            field.parentElement.classList.add('border-error/50');
        }
    }
}

function hideError() {
    const errorDiv = document.getElementById('inputError');
    if (errorDiv) errorDiv.classList.add('hidden');
    ['loginInput', 'passwordInput'].forEach(id => {
        const field = document.getElementById(id);
        if (field && field.parentElement) {
            field.parentElement.classList.remove('border-error/50');
        }
    });
}

function showSuccessMessage(message) {
    const button = document.querySelector('button[type="submit"]');
    if (!button) return;
    const originalText = button.textContent;
    button.textContent = message;
    button.disabled = true;
    setTimeout(() => {
        button.textContent = originalText;
        button.disabled = false;
    }, 2000);
}

// ─── Form Submit ──────────────────────────────────────────────────────────────

document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();
    hideError();

    const inputEl    = document.getElementById('loginInput');
    const passwordEl = document.getElementById('passwordInput');
    const value      = inputEl    ? inputEl.value.trim()    : '';
    const password   = passwordEl ? passwordEl.value        : '';

    if (!value) {
        showError('E-posta veya telefon alanı zorunludur.', 'loginInput');
        inputEl && inputEl.focus();
        return;
    }
    if (!validateEmailOrPhone(value)) {
        showError('Geçerli bir e-posta adresi veya telefon numarası giriniz.', 'loginInput');
        inputEl && inputEl.focus();
        return;
    }
    if (!validatePassword(password)) {
        showError('Şifre en az 6 karakter olmalıdır.', 'passwordInput');
        passwordEl && passwordEl.focus();
        return;
    }

    showSuccessMessage('Giriş yapılıyor...');

    // ── Demo: Gerçek uygulamada API çağrısı yapılır ──────────────────────────
    // fetch('/api/auth/login', { method: 'POST', body: JSON.stringify({eposta: value, sifre: password}) })
    //   .then(r => r.json())
    //   .then(data => {
    //       oturumuKaydet(data.kullaniciId, data.kullaniciAdi, data.rolId);
    //       window.location.href = ROL_YONLENDIRME[data.rolId];
    //   });

    // Demo simülasyonu: Admin olarak giriş
    const demoRolId = 1; // Genel Müdür — gerçek uygulamada API'den gelir
    oturumuKaydet(1, 'Demo Kullanıcı', demoRolId);

    setTimeout(() => {
        const hedef = ROL_YONLENDIRME[demoRolId] || './admin_dashboard/adminDashboard.html';
        window.location.href = hedef;
    }, 1000);
});

// Input'a odaklandığında hatayı gizle
['loginInput', 'passwordInput'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.addEventListener('focus', hideError);
});

// ─── Dashboard Menü Gizleme (tüm dashboardlardan çağrılır) ───────────────────

/**
 * Kullanıcının rolüne göre yan menü öğelerini göster/gizle.
 *
 * Her dashboard HTML'sinde menü öğelerine şu data attribute eklenmeli:
 *   <a data-rol-gereklisi="SUPER,YONETICI" href="...">Finans</a>
 *   <a data-departman-gereklisi="FINANS" href="...">Finans Raporu</a>
 *
 * Bu fonksiyon sayfa yüklendiğinde çağrılır:
 *   document.addEventListener('DOMContentLoaded', menuOgeleriFiltrele);
 */
function menuOgeleriFiltrele() {
    const oturum = oturumuAl();
    if (!oturum) {
        // Oturum yoksa giriş sayfasına gönder
        window.location.href = '../login_register/loginRegister.html';
        return;
    }

    // Yetki seviyesi gerektiren öğeler
    document.querySelectorAll('[data-rol-gereklisi]').forEach(el => {
        const izinliYetkiler = el.dataset.rolGereklisi.split(',').map(s => s.trim());
        if (!izinliYetkiler.includes(oturum.yetki)) {
            el.style.display = 'none';
        }
    });

    // Belirli departman gerektiren öğeler
    document.querySelectorAll('[data-departman-gereklisi]').forEach(el => {
        const gereklisi = el.dataset.departmanGereklisi.trim();
        // SUPER her departmanı görebilir
        if (oturum.yetki === 'SUPER') return;
        if (oturum.departman !== gereklisi) {
            el.style.display = 'none';
        }
    });

    // Kullanıcı adını sidebar'da göster
    const adEtiketi = document.getElementById('oturum-kullanici-adi');
    if (adEtiketi) adEtiketi.textContent = oturum.kullaniciAdi;

    const rolEtiketi = document.getElementById('oturum-rol-adi');
    if (rolEtiketi) rolEtiketi.textContent = oturum.rolAdi;
}

// Bu satırı her dashboard HTML'sinin <script> bloğuna ekleyin:
// document.addEventListener('DOMContentLoaded', menuOgeleriFiltrele);
