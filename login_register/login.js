function roleToDashboardByCode(roleCode) {
    const normalized = String(roleCode || '').toUpperCase();
    if (normalized === 'ADMIN')              return '../login_register/admin_dashboard/adminDashboard.html';
    if (normalized === 'MUDUR')              return '../login_register/admin_dashboard/adminDashboard.html';
    if (normalized === 'BT_SORUMLU')         return '../it_dashboard/it.html';
    if (normalized === 'IK_SORUMLU')         return '../hr_dashboard/hr.html';
    if (normalized === 'URETIM_SORUMLU')     return '../production_dashboard/production.html';
    if (normalized === 'DEPO_SORUMLU')       return '../stock_dashboard/stock.html';
    if (normalized === 'SATIN_ALMA_SORUMLU') return '../purchasing_dashboard/purchasing.html';
    if (normalized === 'FINANS_SORUMLU')     return '../finance_dashboard/finance.html';
    return '../login_register/admin_dashboard/adminDashboard.html';
}

function roleToSessionMeta(roleCode) {
    const normalized = String(roleCode || '').toUpperCase();
    const map = {
        ADMIN:              { yetki: 'SUPER',    departman: 'ADMIN' },
        MUDUR:              { yetki: 'SUPER',    departman: 'MUDUR' },
        BT_SORUMLU:         { yetki: 'PERSONEL', departman: 'BT' },
        IK_SORUMLU:         { yetki: 'PERSONEL', departman: 'IK' },
        URETIM_SORUMLU:     { yetki: 'PERSONEL', departman: 'URETIM' },
        DEPO_SORUMLU:       { yetki: 'PERSONEL', departman: 'DEPO' },
        SATIN_ALMA_SORUMLU: { yetki: 'PERSONEL', departman: 'SATIN_ALMA' },
        FINANS_SORUMLU:     { yetki: 'PERSONEL', departman: 'FINANS' }
    };
    return map[normalized] || { yetki: 'PERSONEL', departman: normalized };
}

function oturumuAl() {
    try { return JSON.parse(localStorage.getItem('oturum')); }
    catch { return null; }
}

function oturumuKapat() {
    localStorage.removeItem('oturum');
    window.location.href = '../login_register/loginRegister.html';
}

function showError(message) {
    const errorDiv = document.getElementById('inputError');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.remove('hidden');
    }
}

function hideError() {
    const errorDiv = document.getElementById('inputError');
    if (errorDiv) errorDiv.classList.add('hidden');
}

function setButtonState(loading) {
    const button = document.querySelector('button[type="submit"]');
    if (!button) return;
    button.disabled = loading;
    button.textContent = loading ? 'Giris yapiliyor...' : 'Giris Yap';
}

document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    hideError();

    const username = document.getElementById('loginInput')?.value.trim();
    const password = document.getElementById('passwordInput')?.value;
    const roleCode = document.getElementById('roleSelect')?.value || 'MUDUR';

    if (!username) {
        showError('Kullanici adi zorunludur.');
        return;
    }
    if (!password || password.length < 4) {
        showError('Sifre en az 4 karakter olmalidir.');
        return;
    }

    setButtonState(true);

    try {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, role: roleCode })
        });

        const data = await res.json();

        if (!res.ok || !data?.success || !data?.data) {
            throw new Error(data?.message || 'Kullanici adi, sifre veya rol hatali.');
        }

        const sessionMeta = roleToSessionMeta(data.data.role || roleCode);
        const oturum = {
            kullaniciAdi: data.data.username || username,
            rolAdi:       data.data.role || roleCode,
            yetki:        sessionMeta.yetki,
            departman:    sessionMeta.departman,
            token:        data.data.token || '',
            menus:        data.data.menus || [],
            girisZamani:  new Date().toISOString()
        };
        localStorage.setItem('oturum', JSON.stringify(oturum));
        window.location.href = roleToDashboardByCode(data.data.role || roleCode);

    } catch (err) {
        showError(err.message || 'Giris sirasinda hata olustu.');
        setButtonState(false);
    }
});

['loginInput', 'passwordInput'].forEach(id => {
    document.getElementById(id)?.addEventListener('focus', hideError);
});

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
    const adEtiketi = document.getElementById('oturum-kullanici-adi');
    if (adEtiketi) adEtiketi.textContent = oturum.kullaniciAdi;
    const rolEtiketi = document.getElementById('oturum-rol-adi');
    if (rolEtiketi) rolEtiketi.textContent = oturum.rolAdi;
}

