import { useMemo, useState } from "react";
import { useLocation } from "react-router-dom";
import { Area, AreaChart, Bar, BarChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis } from "recharts";
import { ArrowRight, CalendarDays, Check, ChevronDown, ChevronRight, CircleDollarSign, CreditCard, Download, FileText, Filter, Gift, MessageSquare, MoreHorizontal, Plus, Search, Send, ShieldCheck, Snowflake, UserPlus, WalletCards, Zap } from "lucide-react";
import { BankCard } from "../components/BankCard";
import { portfolio, spendData, transactions } from "../data";

const pageMeta: Record<string, [string, string]> = {
  "/accounts": ["Accounts", "All your financial relationships in one place."],
  "/transactions": ["Transactions", "Search, filter, and understand every movement."],
  "/transfers": ["Move money", "Fast, secure transfers with complete visibility."],
  "/bill-pay": ["Bill pay", "Manage every payment and never miss a due date."],
  "/cards": ["Card center", "Control your cards, rewards, and security."],
  "/statements": ["Documents", "Statements, tax forms, and notices."],
  "/offers": ["Core Privileges", "Offers selected around how you live."],
  "/support-chat": ["Support", "Expert help whenever you need it."]
};

function Head({ title, copy, action }: { title: string; copy: string; action?: string }) {
  return <div className="page-heading"><div><span>Core digital banking</span><h1>{title}</h1><p>{copy}</p></div>{action && <button className="button button-dark"><Plus size={16} />{action}</button>}</div>;
}

export function PortalPage() {
  const { pathname } = useLocation();
  const [title, copy] = pageMeta[pathname] || pageMeta["/accounts"];
  if (pathname === "/transactions") return <Transactions />;
  if (pathname === "/transfers") return <Transfers />;
  if (pathname === "/bill-pay") return <BillPay />;
  if (pathname === "/cards") return <Cards />;
  if (pathname === "/statements") return <Statements />;
  if (pathname === "/offers") return <Offers />;
  if (pathname === "/support-chat") return <Support />;
  return <Accounts title={title} copy={copy} />;
}

function Accounts({ title, copy }: { title: string; copy: string }) {
  return <><Head title={title} copy={copy} action="Open account" /><div className="account-summary-grid">{[
    ["Everyday", "Core Current", "••4680", "$12,840.20", "Available balance", "#0c2b23"],
    ["Savings", "Core Reserve", "••2214", "$48,650.60", "4.80% variable APY", "#8c7038"],
    ["Investing", "Core Portfolio", "••7290", "$22,800.00", "+8.4% all time", "#4b6070"],
    ["Credit", "Core Altitude", "••9142", "-$2,460.18", "$17,539 available", "#533b45"]
  ].map(x => <article className="account-summary-card" key={x[1]} style={{ background: x[5] }}><span>{x[0]}</span><h3>{x[1]}</h3><small>{x[2]}</small><b>{x[3]}</b><em>{x[4]}</em><button><MoreHorizontal /></button></article>)}</div><div className="dashboard-grid"><div className="panel spending-panel"><div className="card-head"><span>Balance history<small>Combined liquid accounts</small></span><select><option>6 months</option></select></div><ResponsiveContainer width="100%" height={280}><AreaChart data={spendData}><defs><linearGradient id="bal" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stopColor="#2c725e" stopOpacity={.3}/><stop offset="1" stopColor="#2c725e" stopOpacity={0}/></linearGradient></defs><CartesianGrid vertical={false} stroke="#edf0ed"/><XAxis dataKey="month" axisLine={false} tickLine={false}/><Tooltip/><Area type="monotone" dataKey="income" stroke="#2c725e" strokeWidth={3} fill="url(#bal)"/></AreaChart></ResponsiveContainer></div><div className="panel portfolio-card"><div className="card-head"><span>Portfolio mix<small>$22,800 invested</small></span></div><ResponsiveContainer width="100%" height={180}><PieChart><Pie data={portfolio} dataKey="value" innerRadius={58} outerRadius={78} paddingAngle={4}>{portfolio.map(p => <Cell key={p.name} fill={p.color}/>)}</Pie><Tooltip/></PieChart></ResponsiveContainer>{portfolio.map(p => <div className="legend-row" key={p.name}><i style={{ background: p.color }}/><span>{p.name}</span><b>{p.value}%</b></div>)}</div></div></>;
}

function Transactions() {
  const [query, setQuery] = useState("");
  const filtered = useMemo(() => transactions.filter(t => t.name.toLowerCase().includes(query.toLowerCase())), [query]);
  return <><Head title="Transactions" copy="Search, filter, and understand every movement." action="Export" /><div className="transaction-controls"><div><Search size={18}/><input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search transactions…" /></div><button><CalendarDays/> Last 30 days <ChevronDown/></button><button><Filter/> All filters</button></div><div className="panel transaction-table"><div className="table-head"><span>Merchant</span><span>Account</span><span>Date</span><span>Category</span><span>Amount</span></div>{filtered.map(t => <div className="table-row" key={t.name}><span><i style={{ background: t.color }}>{t.icon}</i><b>{t.name}</b></span><span>Core Current ••4680</span><span>{t.date}, 2026</span><span><em>{t.category}</em></span><span className={t.amount > 0 ? "credit" : ""}>{t.amount > 0 ? "+" : "-"}${Math.abs(t.amount).toFixed(2)}</span></div>)}</div></>;
}

function Transfers() {
  const [sent, setSent] = useState(false);
  return <><Head title="Move money" copy="Fast, secure transfers with complete visibility." action="Add recipient" /><div className="transfer-layout"><div className="panel transfer-form"><div className="transfer-tabs"><button className="active">Internal</button><button>External bank</button><button>International</button></div>{sent ? <div className="transfer-success"><i><Check/></i><h2>Transfer scheduled</h2><p>$500.00 will arrive in Core Reserve instantly.</p><button className="button button-dark" onClick={() => setSent(false)}>Make another transfer</button></div> : <><label>From account<button><WalletCards/><span><b>Core Current ••4680</b><small>$12,840.20 available</small></span><ChevronDown/></button></label><label>To account<button><CircleDollarSign/><span><b>Core Reserve ••2214</b><small>$48,650.60 balance</small></span><ChevronDown/></button></label><label>Amount<div className="money-input"><span>$</span><input defaultValue="500.00"/></div></label><div className="transfer-options"><span><Zap/>Arrives instantly</span><span>No transfer fee</span></div><button className="button button-dark auth-submit" onClick={() => setSent(true)}>Review transfer <ArrowRight/></button></>}</div><div className="transfer-side"><div className="panel recipients"><div className="card-head"><span>Frequent recipients</span><button><UserPlus/></button></div>{["Jordan Lee","Maya Patel","Aster Properties"].map((x,i)=><button key={x}><i>{x.split(" ").map(s=>s[0]).join("")}</i><span><b>{x}</b><small>{i===2?"External account":"Core client"}</small></span><Send/></button>)}</div><div className="panel safety-note"><ShieldCheck/><span><b>Protected transfers</b><small>Every transfer is monitored by Core Signal fraud intelligence.</small></span></div></div></div></>;
}

function BillPay() {
  return <><Head title="Bill pay" copy="Manage every payment and never miss a due date." action="Add biller" /><div className="bill-summary"><div><span>Due this month</span><b>$1,842.60</b><small>Across 5 scheduled payments</small></div><div><span>Paid this month</span><b>$2,410.20</b><small>7 payments complete</small></div><div><span>Next payment</span><b>Jun 18</b><small>Northline Internet · $84.00</small></div></div><div className="panel bill-list"><div className="card-head"><span>Upcoming payments<small>June 2026</small></span><button>Manage autopay</button></div>{[["NP","Northstar Power","Jun 17","$142.80","Autopay"],["NI","Northline Internet","Jun 18","$84.00","Scheduled"],["AP","Aster Properties","Jun 22","$1,460.00","Autopay"],["CS","Core Altitude","Jun 28","$155.80","Minimum due"]].map(x=><div className="bill-row" key={x[1]}><i>{x[0]}</i><span><b>{x[1]}</b><small>{x[4]}</small></span><em>{x[2]}</em><strong>{x[3]}</strong><button><MoreHorizontal/></button></div>)}</div></>;
}

function Cards() {
  const [frozen, setFrozen] = useState(false);
  return <><Head title="Card center" copy="Control your cards, rewards, and security." action="Add card" /><div className="card-center"><div className="card-stage"><BankCard platinum/><div className="card-select-dots"><i className="active"/><i/></div></div><div className="panel card-controls"><div className="card-head"><span>Core Altitude ••9142<small>Platinum credit</small></span><span className="active-pill">{frozen ? "Frozen" : "Active"}</span></div><div className="limit-block"><span><small>Current balance</small><b>$2,460.18</b></span><span><small>Credit limit</small><b>$20,000.00</b></span><div><i style={{width:"12%"}}/></div><em>12% utilized</em></div><div className="control-grid"><button onClick={()=>setFrozen(!frozen)}><i>{frozen?<Zap/>:<Snowflake/>}</i><span><b>{frozen?"Unfreeze card":"Freeze card"}</b><small>Instant control</small></span></button><button><i><CreditCard/></i><span><b>Card details</b><small>View securely</small></span></button><button><i><Plus/></i><span><b>Add to wallet</b><small>Digital wallet</small></span></button><button><i><ShieldCheck/></i><span><b>Security</b><small>Limits & alerts</small></span></button></div></div></div><div className="dashboard-grid"><div className="panel spending-panel"><div className="card-head"><span>Card spending<small>This billing period</small></span></div><ResponsiveContainer width="100%" height={220}><BarChart data={spendData}><CartesianGrid vertical={false} stroke="#edf0ed"/><XAxis dataKey="month" axisLine={false} tickLine={false}/><Bar dataKey="spend" fill="#2c725e" radius={[5,5,0,0]}/><Tooltip/></BarChart></ResponsiveContainer></div><div className="panel reward-box"><Gift/><span>AVAILABLE REWARDS</span><b>32,480</b><small>Altitude points · Worth up to $486</small><button>Explore rewards <ArrowRight/></button></div></div></>;
}

function Statements() {
  return <><Head title="Documents" copy="Statements, tax forms, and notices." /><div className="document-tabs"><button className="active">Statements</button><button>Tax documents</button><button>Notices</button></div><div className="panel documents"><div className="card-head"><span>2026 statements<small>Core Current ••4680</small></span><select><option>All accounts</option></select></div>{["May 2026","April 2026","March 2026","February 2026","January 2026"].map((x,i)=><div className="document-row" key={x}><i><FileText/></i><span><b>{x} statement</b><small>PDF · {4.2+i*.3} MB · Generated Jun {i+1}</small></span><button><Download/> Download</button></div>)}</div></>;
}

function Offers() {
  return <><Head title="Core Privileges" copy="Offers selected around how you live." /><div className="offers-hero"><span>MEMBER EXCLUSIVE</span><h2>32,480 points.<br/><em>Countless possibilities.</em></h2><p>Curated travel, dining, and everyday value, all in one place.</p><button className="button button-gold">Explore rewards <ArrowRight/></button></div><div className="offer-grid">{[["Travel","The Aurelian Hotels","Enjoy a fourth night with our compliments.","#102e27"],["Dining","Field & Form","Earn 8× points on your next experience.","#543d32"],["Everyday","Northstar Market","Receive $25 back on $150.","#36475c"]].map(x=><article key={x[1]} style={{background:x[3]}}><small>{x[0]}</small><h3>{x[1]}</h3><p>{x[2]}</p><button>Activate offer <ArrowRight/></button></article>)}</div></>;
}

function Support() {
  const [messages,setMessages]=useState(["Hi Avery. I’m Cora, your Core digital concierge. How can I help today?"]);
  return <><Head title="Support" copy="Expert help whenever you need it." /><div className="support-layout"><div className="panel support-chat"><div className="support-head"><i><MessageSquare/></i><span><b>Cora · Digital concierge</b><small><em/> Online · Replies instantly</small></span><button><MoreHorizontal/></button></div><div className="support-messages">{messages.map((m,i)=><div key={i} className={i%2?"mine":""}>{m}<small>{i?"Just now":"8:42 AM"}</small></div>)}</div><div className="support-compose"><input placeholder="Ask about your accounts…" onKeyDown={e=>{if(e.key==="Enter"&&e.currentTarget.value){setMessages([...messages,e.currentTarget.value,"I found the relevant information and can guide you through the next step securely."]);e.currentTarget.value=""}}}/><button><Send/></button></div></div><div className="support-side"><div className="panel"><b>Popular topics</b>{["Dispute a transaction","Replace a card","Transfer status","Update personal details"].map(x=><button key={x}>{x}<ChevronRight/></button>)}</div><div className="panel care-card"><i><ShieldCheck/></i><b>Need a person?</b><p>Core Care specialists are available 24/7.</p><button>Connect to a specialist</button></div></div></div></>;
}
