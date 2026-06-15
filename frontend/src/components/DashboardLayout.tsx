import { Bell, ChevronDown, LogOut, Menu, Search } from "lucide-react";
import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { navItems } from "../data";
import { Brand } from "./Brand";
import { ChatWidget } from "./ChatWidget";

export function DashboardLayout() {
  const [mobile, setMobile] = useState(false);
  const navigate = useNavigate();
  return (
    <div className="portal-shell">
      <aside className={mobile ? "open" : ""}>
        <Brand />
        <div className="portal-profile">
          <div className="avatar">AM</div><span><small>Welcome back</small>Avery Morgan</span><ChevronDown size={15} />
        </div>
        <nav>{navItems.map(item => <NavLink key={item.path} to={item.path} end={item.path === "/dashboard"} onClick={() => setMobile(false)}><item.icon size={18} /><span>{item.name}</span></NavLink>)}</nav>
        <div className="sidebar-foot">
          <div><span className="secure-dot" />Protected session<small>Last login · Today, 8:41 AM</small></div>
          <button onClick={() => navigate("/")}><LogOut size={17} /> Sign out</button>
        </div>
      </aside>
      <section className="portal-main">
        <header className="portal-header">
          <button className="mobile-portal-menu" onClick={() => setMobile(!mobile)}><Menu /></button>
          <div className="portal-search"><Search size={18} /><input placeholder="Search accounts, transactions, help…" /><kbd>⌘ K</kbd></div>
          <div className="portal-head-actions"><span className="status-pill"><i /> All systems operational</span><button><Bell size={19} /><em>3</em></button><div className="avatar small">AM</div></div>
        </header>
        <div className="portal-content"><Outlet /></div>
      </section>
      <ChatWidget />
    </div>
  );
}
