// Global role-based route guard.
// Admin/super can see all dashboards; department users are constrained.

(function roleGuard() {
  const LOGIN_PAGE = "/login_register/loginRegister.html";
  const ROLE_TO_HOME = {
    SUPER: "/login_register/admin_dashboard/adminDashboard.html",
    ADMIN: "/login_register/admin_dashboard/adminDashboard.html",
    BT: "/it_dashboard/it.html",
    IK: "/hr_dashboard/hr.html",
    URETIM: "/production_dashboard/production.html",
    DEPO: "/stock_dashboard/stock.html",
    SATIN_ALMA: "/purchasing_dashboard/purchasing.html",
    FINANS: "/finance_dashboard/finance.html",
  };

  const DASHBOARD_PREFIX_TO_ROLE = [
    ["/it_dashboard/", "BT"],
    ["/hr_dashboard/", "IK"],
    ["/production_dashboard/", "URETIM"],
    ["/stock_dashboard/", "DEPO"],
    ["/purchasing_dashboard/", "SATIN_ALMA"],
    ["/finance_dashboard/", "FINANS"],
    ["/maintenance_dashboard/", "URETIM"],
    ["/login_register/admin_dashboard/", "SUPER"],
  ];

  function normalizeRole(rawRole, rawDepartment, rawAuthRole) {
    const role = String(rawRole || "").toUpperCase();
    const dep = String(rawDepartment || rawAuthRole || "").toUpperCase();
    if (role === "SUPER" || role === "ADMIN" || dep === "MUDUR") return "SUPER";
    if (dep.includes("BT")) return "BT";
    if (dep.includes("IK")) return "IK";
    if (dep.includes("URETIM")) return "URETIM";
    if (dep.includes("DEPO")) return "DEPO";
    if (dep.includes("SATIN_ALMA") || dep.includes("SATINALMA")) return "SATIN_ALMA";
    if (dep.includes("FINANS")) return "FINANS";
    return role || dep || "";
  }

  function readSession() {
    try {
      return JSON.parse(localStorage.getItem("oturum") || "null");
    } catch {
      return null;
    }
  }

  function currentPath() {
    return (location.pathname || "").replace(/\\/g, "/");
  }

  function roleForPath(path) {
    for (const [prefix, role] of DASHBOARD_PREFIX_TO_ROLE) {
      if (path.includes(prefix)) return role;
    }
    return "";
  }

  function redirectTo(path) {
    if (!path) return;
    const target = path.startsWith("/") ? path : `/${path}`;
    if (!currentPath().includes(target)) {
      location.href = target;
    }
  }

  function isLoginLike(path) {
    return path.includes("/login_register/loginRegister.html") || path.endsWith("/index.html") || path.endsWith("/");
  }

  function init() {
    const path = currentPath();
    const session = readSession();

    if (!session) {
      // No session: allow login/index only.
      if (!isLoginLike(path)) redirectTo(LOGIN_PAGE);
      return;
    }

    const role = normalizeRole(session.yetki, session.departman, session.role);
    const requiredRole = roleForPath(path);

    if (!requiredRole) return;
    if (role === "SUPER") return;
    if (role !== requiredRole) {
      redirectTo(ROLE_TO_HOME[role] || LOGIN_PAGE);
      return;
    }

    // Hide links to unauthorized dashboards in navigation.
    const blockedPrefixes = DASHBOARD_PREFIX_TO_ROLE
      .filter(([, r]) => r !== role)
      .map(([prefix]) => prefix);

    document.querySelectorAll("a[href]").forEach((a) => {
      const href = a.getAttribute("href") || "";
      const normalized = href.replace(/\\/g, "/");
      if (blockedPrefixes.some((prefix) => normalized.includes(prefix))) {
        a.style.display = "none";
      }
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();


