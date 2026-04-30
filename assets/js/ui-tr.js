// Common Turkish UI initializer for all dashboards.
// Keep original texts/layout untouched; only stabilize locale/meta behavior.

(function initTurkishUi() {
  try {
    const root = document.documentElement;
    root.setAttribute("lang", "tr");
    root.setAttribute("translate", "no");
    root.classList.add("notranslate");

    let meta = document.querySelector("meta[name='google']");
    if (!meta) {
      meta = document.createElement("meta");
      meta.setAttribute("name", "google");
      document.head.appendChild(meta);
    }
    meta.setAttribute("content", "notranslate");
  } catch {
    // no-op
  }

  const path = (location.pathname || "").replace(/\\/g, "/");
  const isPublicPage =
    path === "/" ||
    path.endsWith("/index.html") ||
    path.endsWith("/hakkimizda.html") ||
    path.endsWith("/destek.html");

  // Load role guard globally except for public landing/support pages.
  if (!isPublicPage && !window.__roleGuardInjected) {
    window.__roleGuardInjected = true;
    const guard = document.createElement("script");
    guard.src = "/assets/js/role-guard.js";
    guard.defer = true;
    document.head.appendChild(guard);
  }

  // Intentionally no text replacement to avoid changing original dashboard copy.
})();


