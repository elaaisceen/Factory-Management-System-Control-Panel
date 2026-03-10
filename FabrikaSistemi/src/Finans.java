public class Finans extends Department {

    public Finans(String sorumluPersonel) {
        super("Finans", sorumluPersonel);
    }

    public void butcePlanla() {
        islemAnimasyonu();
        System.out.println("Aylık fabrika bütçesi ve ödenekler planlandı.");
    }

    public void muhasebeIslemi() {
        islemAnimasyonu();
        System.out.println("Faturalar ve giderler muhasebeleştirildi.");
    }
}
