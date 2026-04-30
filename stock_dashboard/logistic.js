// Stock Dashboard - Lojistik JavaScript
// Lojistik İşlemleri

document.addEventListener('DOMContentLoaded', function() {
    console.log('Lojistik sayfası yüklendi');
    
    // Oturum bilgilerini yükle
    function oturumuAl() {
        try { return JSON.parse(localStorage.getItem('oturum')); }
        catch { return null; }
    }
    
    const oturum = oturumuAl();
    if (oturum && oturum.kullaniciAdi) {
        const adEtiketi = document.getElementById('oturum-kullanıcı-adi');
        if (adEtiketi) adEtiketi.textContent = oturum.kullaniciAdi;
        const rolEtiketi = document.getElementById('oturum-rol-adi');
        if (rolEtiketi) rolEtiketi.textContent = oturum.rolAdi;
    }
});
