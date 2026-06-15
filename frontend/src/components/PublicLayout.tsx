import { Menu, Search, X } from "lucide-react";
import { useState } from "react";
import { Link, NavLink, Outlet } from "react-router-dom";
import { AnimatePresence, motion } from "framer-motion";
import { Brand } from "./Brand";
import { ChatWidget } from "./ChatWidget";

const menu = [
  ["Bank", "/personal"], ["Business", "/business"], ["Cards", "/credit-cards"],
  ["Borrow", "/loans"], ["Invest", "/investments"], ["Rewards", "/rewards"]
];

export function PublicLayout() {
  const [open, setOpen] = useState(false);
  return (
    <div className="public-shell">
      <header className="public-header">
        <Brand />
        <nav>{menu.map(([name, path]) => <NavLink key={path} to={path}>{name}</NavLink>)}</nav>
        <div className="header-actions">
          <button className="icon-button" aria-label="Search"><Search size={19} /></button>
          <Link className="text-link" to="/customer-care">Support</Link>
          <Link className="button button-light compact" to="/login">Sign in</Link>
          <button className="menu-button" onClick={() => setOpen(!open)}>{open ? <X /> : <Menu />}</button>
        </div>
      </header>
      <AnimatePresence>{open && <motion.div className="mobile-menu" initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: "auto" }} exit={{ opacity: 0, height: 0 }}>{menu.map(([name, path]) => <Link key={path} to={path} onClick={() => setOpen(false)}>{name}</Link>)}</motion.div>}</AnimatePresence>
      <main><Outlet /></main>
      <footer>
        <div className="footer-top"><Brand /><p>Banking with intelligence, reach, and a distinctly human point of view.</p><Link to="/register" className="button button-gold">Become a client</Link></div>
        <div className="footer-grid">
          <div><b>Bank</b><Link to="/checking">Checking</Link><Link to="/savings">Savings</Link><Link to="/credit-cards">Credit cards</Link></div>
          <div><b>Plan</b><Link to="/mortgage">Mortgage</Link><Link to="/loans">Loans</Link><Link to="/investments">Investments</Link></div>
          <div><b>Core Global</b><Link to="/about">About us</Link><Link to="/contact">Contact</Link><Link to="/customer-care">Customer care</Link></div>
          <div><b>Security</b><span>Privacy center</span><span>Fraud prevention</span><span>Accessibility</span></div>
        </div>
        <div className="footer-bottom"><span>© 2026 Core Global Bank. Portfolio demonstration.</span><span>Member FDIC simulation · Equal Housing Opportunity</span></div>
      </footer>
      <ChatWidget />
    </div>
  );
}

