import {
  Landmark, Building2, CreditCard, WalletCards, PiggyBank, HandCoins, Home,
  TrendingUp, Gift, CircleHelp, Headphones, ShieldCheck
} from "lucide-react";

export type Product = {
  slug: string;
  title: string;
  eyebrow: string;
  description: string;
  metric: string;
  metricLabel: string;
  icon: typeof Landmark;
  accent: string;
  benefits: string[];
};

export const products: Product[] = [
  { slug: "personal", title: "Personal banking, centered on you.", eyebrow: "Everyday banking", description: "An intelligent financial home that makes every decision clearer and every day simpler.", metric: "4.80%", metricLabel: "APY on Core Reserve", icon: Landmark, accent: "#7ce2bd", benefits: ["No monthly maintenance fees", "Early direct deposit", "24/7 global support"] },
  { slug: "business", title: "Move your business forward.", eyebrow: "Business banking", description: "Treasury, payments, and working capital built to keep ambitious companies in motion.", metric: "190+", metricLabel: "markets connected", icon: Building2, accent: "#c8a55a", benefits: ["Real-time cash position", "Role-based team controls", "Integrated payment rails"] },
  { slug: "credit-cards", title: "A card with uncommon reach.", eyebrow: "Core Altitude", description: "Premium travel, elevated rewards, and confidence wherever your plans take you.", metric: "5×", metricLabel: "points on travel", icon: CreditCard, accent: "#d9dce5", benefits: ["Global lounge access", "No foreign transaction fee", "Real-time card controls"] },
  { slug: "checking", title: "Your money, always in motion.", eyebrow: "Core Current", description: "A modern checking account with instant insights and none of the friction.", metric: "$0", metricLabel: "monthly fee", icon: WalletCards, accent: "#7ce2bd", benefits: ["Instant transfers", "Smart balance alerts", "Fee-free ATM network"] },
  { slug: "savings", title: "Let every dollar do more.", eyebrow: "Core Reserve", description: "High-yield savings with automated goals and a beautifully simple view of progress.", metric: "4.80%", metricLabel: "variable APY", icon: PiggyBank, accent: "#c8a55a", benefits: ["Automated saving rules", "Unlimited goal spaces", "FDIC insurance simulation"] },
  { slug: "loans", title: "Financing with clarity.", eyebrow: "Core Flex", description: "Flexible personal financing with transparent terms and a fast, human-centered process.", metric: "6.49%", metricLabel: "rates from APR", icon: HandCoins, accent: "#ffb28b", benefits: ["No origination fees", "Flexible payment dates", "Instant pre-qualification"] },
  { slug: "mortgage", title: "Bring your next home closer.", eyebrow: "Core Home", description: "A guided mortgage experience pairing digital speed with expert support.", metric: "24h", metricLabel: "pre-approval target", icon: Home, accent: "#d9dce5", benefits: ["Dedicated advisor", "Digital document center", "Clear milestone tracking"] },
  { slug: "investments", title: "Build wealth with perspective.", eyebrow: "Core Private Markets", description: "A connected investing experience for long-term plans and everyday opportunities.", metric: "$0", metricLabel: "online equity trades", icon: TrendingUp, accent: "#7ce2bd", benefits: ["Curated portfolios", "Goal-based planning", "Advisor collaboration"] },
  { slug: "rewards", title: "Value that follows you.", eyebrow: "Core Privileges", description: "Distinctive experiences and tailored offers selected around how you live.", metric: "32K", metricLabel: "points ready", icon: Gift, accent: "#c8a55a", benefits: ["Personalized offers", "Flexible redemption", "Member-only experiences"] },
  { slug: "customer-care", title: "Real help, at every hour.", eyebrow: "Core Care", description: "Expert support and proactive guidance, available on your terms.", metric: "<30s", metricLabel: "average response", icon: Headphones, accent: "#7ce2bd", benefits: ["24/7 priority support", "Secure in-app messaging", "Fraud response team"] }
];

export const transactions = [
  { name: "Northstar Market", category: "Groceries", date: "Jun 15", amount: -86.42, icon: "NM", color: "#e9b96e" },
  { name: "Payroll · Acme Studio", category: "Income", date: "Jun 14", amount: 4820.00, icon: "AS", color: "#7ce2bd" },
  { name: "Arc Electric", category: "Utilities", date: "Jun 13", amount: -124.18, icon: "AE", color: "#8b9eff" },
  { name: "Solstice Airways", category: "Travel", date: "Jun 12", amount: -620.40, icon: "SA", color: "#ff9f83" },
  { name: "Core Reserve", category: "Transfer", date: "Jun 11", amount: -500.00, icon: "CR", color: "#c8a55a" },
  { name: "Juniper Coffee", category: "Dining", date: "Jun 10", amount: -8.75, icon: "JC", color: "#d5a37f" }
];

export const spendData = [
  { month: "Jan", spend: 3200, income: 5800 }, { month: "Feb", spend: 2900, income: 6100 },
  { month: "Mar", spend: 4100, income: 5900 }, { month: "Apr", spend: 3500, income: 6400 },
  { month: "May", spend: 3800, income: 6200 }, { month: "Jun", spend: 2740, income: 6600 }
];

export const portfolio = [
  { name: "US Equities", value: 48, color: "#7ce2bd" }, { name: "Global Equity", value: 22, color: "#c8a55a" },
  { name: "Fixed Income", value: 18, color: "#6886a5" }, { name: "Alternatives", value: 12, color: "#f0dcb0" }
];

export const navItems = [
  { name: "Overview", path: "/dashboard", icon: Landmark },
  { name: "Accounts", path: "/accounts", icon: WalletCards },
  { name: "Transactions", path: "/transactions", icon: TrendingUp },
  { name: "Transfers", path: "/transfers", icon: HandCoins },
  { name: "Bill pay", path: "/bill-pay", icon: Home },
  { name: "Cards", path: "/cards", icon: CreditCard },
  { name: "Statements", path: "/statements", icon: ShieldCheck },
  { name: "Offers", path: "/offers", icon: Gift },
  { name: "Support", path: "/support-chat", icon: CircleHelp }
];

