import { AnimatePresence, motion } from "framer-motion";
import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import { DashboardLayout } from "./components/DashboardLayout";
import { PublicLayout } from "./components/PublicLayout";
import { Admin } from "./pages/Admin";
import { AuthPage } from "./pages/Auth";
import { Dashboard } from "./pages/Dashboard";
import { Home } from "./pages/Home";
import { InfoPage } from "./pages/InfoPage";
import { PortalPage } from "./pages/PortalPages";
import { ProductPage } from "./pages/ProductPage";

const productRoutes = ["personal", "business", "credit-cards", "checking", "savings", "loans", "mortgage", "investments", "rewards", "customer-care"];
const portalRoutes = ["accounts", "transactions", "transfers", "bill-pay", "cards", "statements", "offers", "support-chat"];

function Page({ children }: { children: React.ReactNode }) {
  return <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} transition={{ duration: .25 }}>{children}</motion.div>;
}

export default function App() {
  const location = useLocation();
  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        <Route element={<PublicLayout />}>
          <Route index element={<Page><Home /></Page>} />
          {productRoutes.map(path => <Route key={path} path={path} element={<Page><ProductPage slug={path} /></Page>} />)}
          <Route path="about" element={<Page><InfoPage /></Page>} />
          <Route path="contact" element={<Page><InfoPage /></Page>} />
        </Route>
        <Route path="/login" element={<Page><AuthPage /></Page>} />
        <Route path="/register" element={<Page><AuthPage register /></Page>} />
        <Route element={<DashboardLayout />}>
          <Route path="/dashboard" element={<Page><Dashboard /></Page>} />
          {portalRoutes.map(path => <Route key={path} path={path} element={<Page><PortalPage /></Page>} />)}
          <Route path="/admin" element={<Page><Admin /></Page>} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AnimatePresence>
  );
}
