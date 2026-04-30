/**
 * userLoader.js - Tüm Dashboardlar İçin Ortak Kullanıcı Bilgisi ve Stil Düzenleyici
 */
document.addEventListener('DOMContentLoaded', function() {
    // 1. İkon Stabilitesi İçin Kritik CSS ve Font Yükleme
    const iconLink = document.createElement('link');
    iconLink.href = 'https://fonts.googleapis.com/icon?family=Material+Icons|Material+Symbols+Outlined';
    iconLink.rel = 'stylesheet';
    document.head.appendChild(iconLink);

    const style = document.createElement('style');
    style.innerHTML = `
        .material-symbols-outlined, .material-icons {
            font-family: 'Material Symbols Outlined', 'Material Icons' !important;
            display: inline-block !important;
            width: 1.2em !important;
            height: 1.2em !important;
            font-size: 24px !important;
            line-height: 1 !important;
            overflow: visible !important;
            text-indent: 0 !important;
            direction: ltr !important;
            text-align: center !important;
            vertical-align: middle !important;
        }
    `;
    document.head.appendChild(style);

    // 2. Kullanıcı Bilgilerini Yükle (Admin Dashboard ve Diğerleri)
    const oturum = JSON.parse(localStorage.getItem('oturum') || '{}');
    
    // ID bazlı eşleşmeler (Yeni eklediklerimiz)
    const nameEl = document.getElementById('user-fullname');
    const roleEl = document.getElementById('user-role');
    
    // Eski sayfalardaki muhtemel ID'ler (it.html, hr.html vb. için)
    const oldNameEl = document.getElementById('oturum-kullanici-adi');
    const oldRolEl = document.getElementById('oturum-rol-adi');

    const fullName = oturum.kullaniciAdi || oturum.fullName || 'Misafir Kullanıcı';
    const roleName = oturum.rolAdi || oturum.roleName || 'Sistem Paneli';

    if (nameEl) nameEl.innerText = fullName;
    if (oldNameEl) oldNameEl.innerText = fullName;

    if (roleEl) {
        roleEl.innerText = roleName.replace(/_/g, ' ').toLowerCase()
                           .replace(/\b\w/g, l => l.toUpperCase());
    }
    if (oldRolEl) {
        oldRolEl.innerText = roleName;
    }

    console.log('[INFO] Kullanıcı bilgileri yüklendi:', fullName);
});

// Global Çıkış Fonksiyonu
function handleLogout() {
    if (confirm("Oturumunuzu sonlandırmak istediğinize emin misiniz?")) {
        localStorage.clear();
        sessionStorage.clear();
        
        // Çıkış yönlendirmesi - Dizin derinliğine göre ayarlanır
        const currentPath = window.location.pathname;
        let target = 'login_register/loginRegister.html';
        
        if (currentPath.includes('admin_dashboard') || currentPath.includes('dashboard')) {
            target = '../../login_register/loginRegister.html';
        } else if (currentPath.includes('it_dashboard') || currentPath.includes('hr_dashboard') || 
                   currentPath.includes('finance_dashboard') || currentPath.includes('production_dashboard') ||
                   currentPath.includes('stock_dashboard') || currentPath.includes('purchasing_dashboard')) {
            target = '../login_register/loginRegister.html';
        }
        
        alert("Oturum kapatıldı.");
        window.location.href = target;
    }
}
