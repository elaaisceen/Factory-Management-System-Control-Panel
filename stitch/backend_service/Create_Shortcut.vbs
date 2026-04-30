Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = oWS.SpecialFolders("Desktop") & "\Fabrika ERP.lnk"
Set oLink = oWS.CreateShortcut(sLinkFile)

' Ana başlatıcı dosyasının yolu
dim projectPath
projectPath = "c:\Users\elanu\Desktop\FactoryManagementSystemControlPanel0_fixed\FactoryManagementSystemControlPanel0\stitch\backend_service"

oLink.TargetPath = projectPath & "\run_desktop.bat"
oLink.WorkingDirectory = projectPath
oLink.Description = "Fabrika ERP - Yönetim ve Takip Sistemi"

' Java İkonunu kullan (Görseldeki gibi)
' Eğer bilgisayarda Java yüklüyse javaw.exe'den ikonu çeker
oLink.IconLocation = "C:\Program Files\Java\jdk-21\bin\javaw.exe, 0"

' Eğer üstteki yol hatalıysa standart bir simge kullan (B planı)
on error resume next
oLink.Save

if err.number <> 0 then
    oLink.IconLocation = "shell32.dll, 164" ' Alternatif sistem ikonu
    oLink.Save
end if

WScript.Echo "Başarılı! Masaüstünde 'Fabrika ERP' kısayolu oluşturuldu."
