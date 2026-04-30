package com.factory.stitch.ui;

import com.factory.stitch.service.MalzemeServisi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Malzeme Yönetimi Paneli.
 *
 * {@link DashboardPanel} soyut sınıfını genişleterek malzeme listesi,
 * kritik stok uyarısı ve stok güncelleme işlemlerini sunar.
 */
public class MalzemePanel extends DashboardPanel {

    private final MalzemeServisi servis = new MalzemeServisi();

    private DefaultTableModel tablo;
    private JLabel durumEtiketi;

    public MalzemePanel() {
        super("Malzeme Yönetimi");
        veriYenile();
    }

    @Override
    protected void panelOlustur() {
        // Araç çubuğu
        JPanel aracCubugu = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JButton btnYenile   = new JButton("Yenile");
        JButton btnKritikRapor = new JButton("Kritik Stok Raporu");
        aracCubugu.add(btnYenile);
        aracCubugu.add(btnKritikRapor);

        btnYenile.addActionListener(e -> veriYenile());
        btnKritikRapor.addActionListener(e -> kritikRaporGoster());

        // Tablo
        String[] sutunlar = {"ID", "Malzeme Adı", "Stok", "Kritik Limit", "Birim", "Durum"};
        tablo = new DefaultTableModel(sutunlar, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable jTable = new JTable(tablo);
        jTable.setRowHeight(24);

        // Durum çubuğu
        durumEtiketi = new JLabel(" ");
        durumEtiketi.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        add(aracCubugu,             BorderLayout.NORTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);
        add(durumEtiketi,            BorderLayout.SOUTH);
    }

    @Override
    public void veriYenile() {
        if (tablo == null) return;
        tablo.setRowCount(0);
        // MalzemeServisi şu an in-memory; gerçek uygulamada DAO/repository kullanılır
        // Örnek satırlar (servis public API'si geliştikçe burası güncellenir)
        servis.malzemeleriListele(); // konsola yazar; ileride List<Malzeme> döndürecek
        durumEtiketi.setText("Son güncelleme: " + java.time.LocalTime.now());
    }

    private void kritikRaporGoster() {
        JOptionPane.showMessageDialog(this,
                "Kritik stok raporu konsola yazıldı.\n(Gelecek sürümde bu pencerede görüntülenecek.)",
                "Kritik Stok", JOptionPane.WARNING_MESSAGE);
        servis.kritikStokRaporu();
    }
}
