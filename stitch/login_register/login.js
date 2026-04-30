/**
 * login.js — Giriş Formu Mantığı + localStorage Tabanlı Auth
 * Veritabanı yoksa kayıtlı kullanıcıları localStorage'dan okur.
 * İlk çalışmada varsayılan admin hesabı oluşturulur.
 */

// ─── Varsayılan Admin Hesabı ───────────────────────────────────────────────────
function varsayilanKullanicilariOlustur() {
    const mevcutlar = JSON.parse(localStorage.getItem('kayitliKullanicilar') || '[]');
    if (mevcutlar.length === 0) {
        const varsayilan = [
            {
                id: 1,
                ad: 'Admin',
                soyad: 'Kullanıcı',
                email: 'admin@fabrika.com',
                telefon: '05551234567',
                sifre: 'admin123',
                rolId: 2,
                rolAdi: 'GELISTIRICI_ADMIN',
                yetki: 'SUPER',
                departman: 'IT',
                kayitTarihi: new Date().toISOString()
            }
        ];
        localStorage.setItem('kayitliKullanicilar', JSON.stringify(varsayilan));
        console.log('[INFO] Varsayılan admin hesabı oluşturuldu: admin@fabrika.com / admin123');
    }
}

// ─── Rol → Dashboard Yönlendirme Haritası ─────────────────────────────────────
const ROL_YONLENDIRME = {
    1:  '../login_register/admin_dashboard/adminDashboard.html',
    2:  '../login_register/admin_dashboard/adminDashboard.html',
    3:  '../it_dashboard/it.html',
    4:  '../it_dashboard/it.html',
    5:  '../hr_dashboard/hr.html',
    6:  '../hr_dashboard/hr.html',
    7:  '../stock_dashboard/stock.html',
    8:  '../stock_dashboard/stock.html',
    9:  '../production_dashboard/production.html',
    10: '../production_dashboard/production.html',
    11: '../finance_dashboard/finance.html',
    12: '../finance_dashboard/finance.html',
    13: '../purchasing_dashboard/purchasing.html',
    14: '../purchasing_dashboard/purchasing.html',
};

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

// ─── Oturum Yönetimi ───────────────────────────────────────────────────────────
function oturumuKaydet(kullanici) {
    const oturum = {
        kullaniciId:  kullanici.id,
        kullaniciAdi: kullanici.ad + ' ' + kullanici.soyad,
        rolId:        kullanici.rolId,
        rolAdi:       kullanici.rolAdi,
        yetki:        kullanici.yetki,
        departman:    kullanici.departman,
        girisZamani:  new Date().toISOString(),
    };
    localStorage.setItem('oturum', JSON.stringify(oturum));
    return oturum;
}

function oturumuAl() {
    try { return JSON.parse(localStorage.getItem('oturum')); }
    catch { return null; }
}

function oturumuKapat() {
    localStorage.removeItem('oturum');
    window.location.href = '../login_register/loginRegister.html';
}

// ─── localStorage Giriş Kontrolü ──────────────────────────────────────────────
function localKullaniciBul(girdi, sifre) {
    const kullanicilar = JSON.parse(localStorage.getItem('kayitliKullanicilar') || '[]');
    const girdiyiTemizle = girdi.trim().toLowerCase().replace(/\s/g, '');

    return kullanicilar.find(k => {
        const emailEslesti  = k.email && k.email.toLowerCase() === girdiyiTemizle;
        const telEslesti    = k.telefon && k.telefon.replace(/\s/g, '') === girdiyiTemizle;
        const sifreEslesti  = k.sifre === sifre;
        return (emailEslesti || telEslesti) && sifreEslesti;
    }) || null;
}

// ─── Validasyon ────────────────────────────────────────────────────────────────
function validateEmailOrPhone(v) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^(\+90|0)?[5][0-9]{9}$/;
    return emailRegex.test(v) || phoneRegex.test(v.replace(/\s/g,''));
}

// ─── UI Yardımcıları ───────────────────────────────────────────────────────────
function showError(divId, msg) {
    const el = document.getElementById(divId);
    if (el) { el.textContent = msg; el.classList.remove('hidden'); }
}
function hideErrors() {
    ['inputError','passwordError'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.classList.add('hidden');
    });
}
function showSuccess(msg) {
    const el = document.getElementById('successBanner');
    if (el) { el.textContent = msg; el.classList.remove('hidden'); }
}
function setButtonLoading(loading) {
    const btn = document.getElementById('loginBtn');
    if (!btn) return;
    btn.disabled = loading;
    btn.textContent = loading ? 'Giriş yapılıyor...' : 'Giriş Yap';
}

// ─── Form Submit ───────────────────────────────────────────────────────────────
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    hideErrors();

    const girdi   = (document.getElementById('loginInput').value || '').trim();
    const sifre   = (document.getElementById('passwordInput').value || '');

    if (!girdi) {
        showError('inputError', 'E-posta veya telefon alanı zorunludur.');
        return;
    }
    if (!validateEmailOrPhone(girdi)) {
        showError('inputError', 'Geçerli bir e-posta adresi veya telefon numarası giriniz.');
        return;
    }
    if (!sifre || sifre.length < 6) {
        showError('passwordError', 'Şifre en az 6 karakter olmalıdır.');
        return;
    }

    setButtonLoading(true);

    // Önce backend dene, çalışmıyorsa localStorage kullan
    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: girdi, password: sifre }),
        signal: AbortSignal.timeout(10000) // 10 sn timeout - İlk açılışta sunucu yavaş olabilir
    })
    .then(r => r.json())
    .then(data => {
        if (data.success) {
            const payload = data.payload;
            const oturum = {
                kullaniciId:  payload.userId,
                kullaniciAdi: payload.fullName,
                rolId:        payload.roleId,
                rolAdi:       payload.roleName,
                yetki:        payload.authority,
                departman:    payload.department,
                token:        payload.token,
                girisZamani:  new Date().toISOString()
            };
            localStorage.setItem('oturum', JSON.stringify(oturum));
            showSuccess('Giriş başarılı! Yönlendiriliyorsunuz...');
            
            // RÜTBELENDİRME VE YÖNLENDİRME MANTIĞI
            let hedef = '../login_register/admin_dashboard/adminDashboard.html'; // Varsayılan

            if (oturum.yetki === 'SUPER') {
                // Genel Müdür ve Admin bütün sistemi görebilir
                hedef = '../login_register/admin_dashboard/adminDashboard.html';
            } else if (oturum.departman === 'IT') {
                // IT personeli bütün IT dashboarduna erişebilir
                hedef = '../it_dashboard/it.html';
            } else if (oturum.yetki === 'YONETICI') {
                // Departman yöneticileri kendi departmanlarının tamamına erişebilir
                hedef = ROL_YONLENDIRME[oturum.rolId] || hedef;
            } else if (oturum.yetki === 'PERSONEL') {
                // Departman personeli, yöneticinin yönlendirdiği alana (Şimdilik departman ana sayfası)
                hedef = ROL_YONLENDIRME[oturum.rolId] || hedef;
                console.log('[INFO] Personel girişi: Yönetici yönlendirmesi bekleniyor...');
            }

            setTimeout(() => window.location.href = hedef, 800);
        } else {
            // Backend "yanlış şifre" dese bile, belki local'de farklıdır (Fallback)
            const kullanici = localKullaniciBul(girdi, sifre);
            if (kullanici) {
                const oturum = oturumuKaydet(kullanici);
                showSuccess('Giriş başarılı (Yerel Bellek)!');
                setTimeout(() => window.location.href = ROL_YONLENDIRME[oturum.rolId] || '../login_register/admin_dashboard/adminDashboard.html', 800);
            } else {
                showError('inputError', data.message || 'E-posta/telefon veya şifre hatalı.');
                setButtonLoading(false);
            }
        }
    })
    .catch(() => {
        // Backend yoksa localStorage'dan kontrol et (Geliştirme için fallback)
        const kullanici = localKullaniciBul(girdi, sifre);
        if (kullanici) {
            const oturum = oturumuKaydet(kullanici);
            showSuccess('Giriş başarılı (Local)! Yönlendiriliyorsunuz...');
            
            let hedef = '../login_register/admin_dashboard/adminDashboard.html';
            if (oturum.yetki === 'SUPER') {
                hedef = '../login_register/admin_dashboard/adminDashboard.html';
            } else if (oturum.departman === 'IT') {
                hedef = '../it_dashboard/it.html';
            } else {
                hedef = ROL_YONLENDIRME[oturum.rolId] || hedef;
            }

            setTimeout(() => window.location.href = hedef, 800);
        } else {
            showError('inputError', 'Sunucuya bağlanılamıyor veya bilgiler hatalı. Lütfen birkaç saniye bekleyip tekrar deneyin.');
            setButtonLoading(false);
        }
    });
});

// Focus'ta hataları gizle
['loginInput', 'passwordInput'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.addEventListener('focus', hideErrors);
});

// ─── Dashboard Menü Filtreleme ────────────────────────────────────────────────
function menuOgeleriFiltrele() {
    const oturum = oturumuAl();
    if (!oturum) {
        window.location.href = '../login_register/loginRegister.html';
        return;
    }
    document.querySelectorAll('[data-rol-gereklisi]').forEach(el => {
        const izinliYetkiler = el.dataset.rolGereklisi.split(',').map(s => s.trim());
        if (!izinliYetkiler.includes(oturum.yetki)) el.style.display = 'none';
    });
    document.querySelectorAll('[data-departman-gereklisi]').forEach(el => {
        const gereklisi = el.dataset.departmanGereklisi.trim();
        if (oturum.yetki === 'SUPER') return;
        if (oturum.departman !== gereklisi) el.style.display = 'none';
    });
    const adEl = document.getElementById('oturum-kullanici-adi');
    if (adEl) adEl.textContent = oturum.kullaniciAdi;
    const rolEl = document.getElementById('oturum-rol-adi');
    if (rolEl) rolEl.textContent = oturum.rolAdi;
}

// Sayfa yüklendiğinde varsayılan kullanıcıları oluştur
varsayilanKullanicilariOlustur();
