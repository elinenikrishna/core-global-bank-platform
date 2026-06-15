import { ArrowDownLeft, ArrowUpRight, Bell, ChevronRight, CreditCard, Eye, EyeOff, HandCoins, MoreHorizontal, Plus, Receipt, Send, ShieldAlert, TrendingUp, WalletCards } from "lucide-react";
import { useState } from "react";
import { Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { BankCard } from "../components/BankCard";
import { spendData, transactions } from "../data";
import { Link } from "react-router-dom";

export function Dashboard() {
  const [visible, setVisible] = useState(true);
  return (
    <div className="dashboard">
      <div className="page-heading"><div><span>Monday, June 15</span><h1>Good morning, Avery.</h1><p>Here’s your financial picture today.</p></div><button className="button button-dark"><Plus size={17} /> Move money</button></div>
      <section className="balance-overview">
        <div className="total-card">
          <div className="total-top"><span>Total balance<small>Across 4 accounts</small></span><button onClick={() => setVisible(!visible)}>{visible ? <Eye size={18} /> : <EyeOff size={18} />}</button></div>
          <b>{visible ? "$84,290.80" : "••••••••"}</b><span className="positive"><TrendingUp size={15} /> +$2,480.12 this month</span>
          <div className="total-breakdown"><span><i style={{ width: "48%" }} /><small>Checking</small><b>$12,840.20</b></span><span><i style={{ width: "78%" }} /><small>Savings</small><b>$48,650.60</b></span><span><i style={{ width: "36%" }} /><small>Investments</small><b>$22,800.00</b></span></div>
        </div>
        <div className="quick-actions">
          <div className="card-head"><span>Quick actions</span><MoreHorizontal /></div>
          <div className="quick-grid"><Link to="/transfers"><i><Send /></i><span>Transfer</span><small>Send money</small></Link><Link to="/bill-pay"><i><Receipt /></i><span>Pay bills</span><small>Due soon</small></Link><Link to="/cards"><i><CreditCard /></i><span>Card controls</span><small>Manage cards</small></Link><Link to="/accounts"><i><Plus /></i><span>Add account</span><small>Connect bank</small></Link></div>
        </div>
      </section>
      <section className="dashboard-grid">
        <div className="panel spending-panel">
          <div className="card-head"><span>Cash flow<small>Income and spending · Last 6 months</small></span><select><option>6 months</option><option>12 months</option></select></div>
          <div className="chart-summary"><span><small>Income</small><b>$36,800</b></span><span><small>Spending</small><b>$20,240</b></span><em>+18.4% net</em></div>
          <ResponsiveContainer width="100%" height={230}><AreaChart data={spendData}><defs><linearGradient id="income" x1="0" y1="0" x2="0" y2="1"><stop offset="5%" stopColor="#2c725e" stopOpacity={.28}/><stop offset="95%" stopColor="#2c725e" stopOpacity={0}/></linearGradient></defs><CartesianGrid vertical={false} stroke="#e9ece8" /><XAxis dataKey="month" axisLine={false} tickLine={false} tick={{ fill: "#84908b", fontSize: 11 }} /><YAxis hide /><Tooltip contentStyle={{ borderRadius: 12, border: "none", boxShadow: "0 8px 25px #0002" }} /><Area type="monotone" dataKey="income" stroke="#29705c" strokeWidth={2.5} fill="url(#income)" /><Area type="monotone" dataKey="spend" stroke="#c8a55a" strokeWidth={2} fill="transparent" strokeDasharray="5 4" /></AreaChart></ResponsiveContainer>
        </div>
        <div className="panel insight-panel"><div className="card-head"><span>Core Insight<small>Personalized for you</small></span><span className="ai-pill">AI</span></div><div className="insight-icon"><TrendingUp /></div><b>You’re on track to save $6,400 more this year.</b><p>Your dining spend is down 18% while automated savings are ahead of plan.</p><button>View savings plan <ChevronRight size={15} /></button><div className="insight-progress"><i /><span>74% of annual goal</span><b>$19.2K / $26K</b></div></div>
      </section>
      <section className="dashboard-grid lower">
        <div className="panel transactions-panel"><div className="card-head"><span>Recent activity<small>Across your accounts</small></span><Link to="/transactions">View all <ChevronRight size={15} /></Link></div>{transactions.slice(0, 5).map(t => <div className="transaction" key={t.name}><i style={{ background: t.color }}>{t.icon}</i><span><b>{t.name}</b><small>{t.category} · {t.date}</small></span><em className={t.amount > 0 ? "credit" : ""}>{t.amount > 0 ? "+" : "-"}${Math.abs(t.amount).toLocaleString("en-US", { minimumFractionDigits: 2 })}</em></div>)}</div>
        <div className="right-stack">
          <div className="panel card-mini"><div className="card-head"><span>Core Altitude</span><Link to="/cards">Manage</Link></div><BankCard small /><div className="credit-row"><span><small>Current balance</small><b>$2,460.18</b></span><span><small>Available credit</small><b>$17,539.82</b></span></div></div>
          <div className="panel alert-card"><i><ShieldAlert /></i><span><b>Your accounts are protected</b><small>No unusual activity detected.</small></span><ChevronRight size={17} /></div>
        </div>
      </section>
      <section className="account-row">{[{ icon: WalletCards, name: "Core Current", no: "••4680", amount: "$12,840.20", meta: "Available balance" }, { icon: HandCoins, name: "Core Reserve", no: "••2214", amount: "$48,650.60", meta: "4.80% APY" }, { icon: ArrowDownLeft, name: "Home loan", no: "••1093", amount: "$318,420.00", meta: "Next payment Jul 1" }, { icon: ArrowUpRight, name: "Rewards", no: "Altitude", amount: "32,480 pts", meta: "Worth $486" }].map(a => <div className="account-tile" key={a.name}><a.icon /><span><b>{a.name}</b><small>{a.no}</small></span><strong>{a.amount}<small>{a.meta}</small></strong><ChevronRight /></div>)}</section>
    </div>
  );
}
