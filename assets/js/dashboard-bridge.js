// Dashboard ekranlari icin backend baglanti koprusu.
// JavaFX icinden acilan WebView senaryosunda window.javaBridge de desteklenir.
(async function dashboardBridge() {
  const API_BASE = "http://localhost:8080";

  // Tarayici otomatik ceviri/zoom sapitmalarini azaltmak icin.
  try {
    document.documentElement.setAttribute("lang", "tr");
    document.documentElement.setAttribute("translate", "no");
    document.documentElement.classList.add("notranslate");
    let meta = document.querySelector("meta[name='google']");
    if (!meta) {
      meta = document.createElement("meta");
      meta.setAttribute("name", "google");
      document.head.appendChild(meta);
    }
    meta.setAttribute("content", "notranslate");
  } catch (_) {
    // no-op
  }

  function setNodeValue(node, value) {
    if (!node) {
      return;
    }
    node.textContent = String(value ?? 0);
  }

  function findValueNodeByTitle(titlePattern) {
    const labels = Array.from(document.querySelectorAll("p,span,h1,h2,h3,h4,h5,h6,div"));
    for (const label of labels) {
      const text = (label.textContent || "").trim();
      if (!text || !titlePattern.test(text)) {
        continue;
      }

      const card = label.closest("div");
      if (!card) {
        continue;
      }

      const numberNode = card.querySelector("h1, h2, h3, .stat-value, .metric-value, [data-erp-value]");
      if (numberNode && numberNode !== label) {
        return numberNode;
      }
    }
    return null;
  }

  function resolveBridgeProvider() {
    if (window.javaBridge && typeof window.javaBridge.getDashboardSummary === "function") {
      return {
        async getSummary() {
          const raw = await window.javaBridge.getDashboardSummary();
          return typeof raw === "string" ? JSON.parse(raw) : raw;
        }
      };
    }

    return {
      async getSummary() {
        const healthRes = await fetch(`${API_BASE}/api/erp/health`);
        const health = await healthRes.json();
        if (!health?.success) {
          return null;
        }

        const summaryRes = await fetch(`${API_BASE}/api/dashboard/summary`);
        const summary = await summaryRes.json();
        return summary?.success ? summary : null;
      }
    };
  }

  try {
    const provider = resolveBridgeProvider();
    const summary = await provider.getSummary();
    if (!summary?.data) {
      return;
    }

    const data = summary.data;

    // 1) Yeni sayfalar icin dogrudan veri attributeleri
    setNodeValue(document.querySelector("[data-erp='active-employees']"), data.toplamPersonel);
    setNodeValue(document.querySelector("[data-erp='critical-stock']"), data.kritikStokAdedi);
    setNodeValue(document.querySelector("[data-erp='active-production']"), data.aktifUretimProjesi);
    setNodeValue(document.querySelector("[data-erp='open-orders']"), data.acikSiparisAdedi);

    // 2) Eski dashboardlar bozulmadan metin bazli otomatik baglama
    const activeEmployeesByLabel = findValueNodeByTitle(/aktif calisanlar|aktif çalışanlar|calisan sayisi|çalışan sayısı/i);
    setNodeValue(activeEmployeesByLabel, data.toplamPersonel);

    const criticalStockByLabel = findValueNodeByTitle(/kritik stok|stok uyar|stok alarm/i);
    setNodeValue(criticalStockByLabel, data.kritikStokAdedi);
  } catch (error) {
    console.debug("Dashboard bridge beklenen sekilde baglanamadi:", error);
  }
})();

