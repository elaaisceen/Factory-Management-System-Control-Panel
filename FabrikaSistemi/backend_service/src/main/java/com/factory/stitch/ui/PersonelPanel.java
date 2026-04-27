package com.factory.stitch.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Personel Yönetimi Paneli.
 *
 * {@link DashboardPanel} soyut sınıfını genişleterek personel listesi
 * ve departman bazlı filtreleme işlemlerini sunar.
 *
 * GÜVENLİK: Bu panel kullanıcı adı / şifre içermez. Yalnızca
 * oturumu zaten açık bir kullanıcının işlemlerini gösterir.
 */
public class PersonelPanel extends DashboardPanel {

    private DefaultTableModel tablo;
    private JLabel durumEtiketi;

    public PersonelPanel() {
        super("Personel Yönetimi");
        veriYenile();
    }

    @Override
    protected void panelOlustur() {
        // Araç çubuğu
        JPanel aracCubugu = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JButton btnYenile = new JButton("Yenile");
        aracCubugu.add(btnYenile);
        btnYenile.addActionListener(e -> veriYenile());

        // Tablo
        String[] sutunlar = { "ID", "Ad Soyad", "Departman", "Maaş (TL)", "İşe Giriş", "Aktif" };
        tablo = new DefaultTableModel(sutunlar, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable jTable = new JTable(tablo);
        jTable.setRowHeight(24);

        durumEtiketi = new JLabel(" ");
        durumEtiketi.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        add(aracCubugu, BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        add(durumEtiketi, BorderLayout.SOUTH);
    }

    @Override
    public void veriYenile() {
        if (tablo == null)
            return;
        tablo.setRowCount(0);
        // TODO: PersonelRepository / DAO bağlandığında burası gerçek veriyle dolacak
        // Şu an schema_mysql.sql'deki örnek veriler yansıtılıyor
        tablo.addRow(new Object[] { 1, "Veli Demir", "İnsan Kaynakları", "32.000,00", "2025-01-10", "Evet" });
        tablo.addRow(new Object[] { 2, "Burak Usta", "Üretim", "38.000,00", "2024-11-03", "Evet" });
        tablo.addRow(new Object[] { 3, "Deniz Yıldız", "Depo ve Stok", "30.000,00", "2025-02-20", "Evet" });
        durumEtiketi.setText("Son güncelleme: " + java.time.LocalTime.now());
    }
}
