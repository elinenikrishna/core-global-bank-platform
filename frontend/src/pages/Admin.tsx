import { Activity, AlertTriangle, ArrowUpRight, CheckCircle2, CloudCog, Database, Radio, type LucideIcon } from "lucide-react";
import { Area, AreaChart, Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis } from "recharts";
import { spendData } from "../data";

const kpis: Array<[string, string, string, LucideIcon]> = [
  ["Total customers", "3.02M", "+12.8%", ArrowUpRight],
  ["Managed accounts", "8.41M", "+9.4%", Database],
  ["24h transaction volume", "$1.28B", "+18.2%", Activity],
  ["Open fraud alerts", "142", "-8.6%", AlertTriangle]
];
const services: Array<[string, string, LucideIcon]> = [
  ["API Gateway", "Healthy", CloudCog], ["Transaction Service", "Healthy", Activity],
  ["Kafka event bus", "Healthy", Radio], ["Cassandra cluster", "Healthy", Database],
  ["Fraud Detection", "Degraded", AlertTriangle]
];

export function Admin() {
  return (
    <div className="admin-page">
      <div className="page-heading"><div><span>Core Command Center</span><h1>Platform intelligence</h1><p>Live operational and business health across the Core ecosystem.</p></div><span className="live-status"><i />Live · Updated 4s ago</span></div>
      <div className="admin-kpis">{kpis.map(([label, value, change, Icon]) => <article key={label}><div><span>{label}</span><Icon /></div><b>{value}</b><small>{change} vs prior period</small></article>)}</div>
      <div className="admin-grid">
        <div className="panel admin-chart"><div className="card-head"><span>Transaction throughput<small>Events processed per minute</small></span><select><option>Last 24 hours</option></select></div><ResponsiveContainer width="100%" height={280}><AreaChart data={spendData}><defs><linearGradient id="adminA" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stopColor="#7ce2bd" stopOpacity={.4} /><stop offset="1" stopColor="#7ce2bd" stopOpacity={0} /></linearGradient></defs><CartesianGrid vertical={false} stroke="#ffffff12" /><XAxis dataKey="month" axisLine={false} tickLine={false} /><Area type="monotone" dataKey="income" stroke="#7ce2bd" strokeWidth={2.5} fill="url(#adminA)" /><Tooltip /></AreaChart></ResponsiveContainer></div>
        <div className="panel service-health"><div className="card-head"><span>Service health<small>11 microservices</small></span><b>99.99% uptime</b></div>{services.map(([name, status, Icon]) => <div key={name}><i><Icon /></i><span><b>{name}</b><small>p95 · {status === "Healthy" ? "42ms" : "188ms"}</small></span><em className={status === "Healthy" ? "" : "warn"}>{status}</em></div>)}</div>
      </div>
      <div className="admin-grid lower">
        <div className="panel admin-chart"><div className="card-head"><span>Product growth<small>New accounts by month</small></span></div><ResponsiveContainer width="100%" height={240}><BarChart data={spendData}><CartesianGrid vertical={false} stroke="#ffffff12" /><XAxis dataKey="month" axisLine={false} tickLine={false} /><Bar dataKey="income" fill="#c8a55a" radius={[5, 5, 0, 0]} /><Tooltip /></BarChart></ResponsiveContainer></div>
        <div className="panel logs-panel"><div className="card-head"><span>System activity<small>Splunk-style live event stream</small></span></div>{[["INFO", "transfer-service", "Transfer settled · correlation=CG-88201"], ["INFO", "notification-service", "Push dispatched · latency=22ms"], ["WARN", "fraud-service", "Velocity rule triggered · score=0.82"], ["INFO", "account-service", "Balance snapshot refreshed"], ["INFO", "kafka", "transaction-created · partition=12"]].map(([level, service, message]) => <div key={message}><em className={level === "WARN" ? "warn" : ""}>{level}</em><span><b>{service}</b><small>{message}</small></span><CheckCircle2 /></div>)}</div>
      </div>
    </div>
  );
}
