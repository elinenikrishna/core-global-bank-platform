import { ArrowRight, Mail, MapPin, Phone, type LucideIcon } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

const contacts: Array<[LucideIcon, string, string, string]> = [
  [Phone, "Call Core Care", "1 800 CORE 24", "Available 24/7"],
  [Mail, "Secure message", "Start a conversation", "Replies within 2 hours"],
  [MapPin, "Visit Core", "Find a private client center", "190+ global markets"]
];

export function InfoPage() {
  const { pathname } = useLocation();
  const contact = pathname === "/contact";
  return <div className="info-page"><section><span className="section-kicker light">{contact ? "Talk with Core" : "Our point of view"}</span><h1>{contact ? <>Here when it<br /><em>matters.</em></> : <>Progress begins<br /><em>with perspective.</em></>}</h1><p>{contact ? "Connect with a Core specialist or find the right team for your needs." : "Core Global Bank brings human judgment and digital intelligence together to help clients move forward with confidence."}</p></section>{contact ? <div className="contact-grid">{contacts.map(([Icon, title, detail, note]) => <article key={title}><Icon /><h3>{title}</h3><b>{detail}</b><p>{note}</p><button>Connect <ArrowRight /></button></article>)}</div> : <div className="about-copy"><span>OUR PURPOSE</span><h2>Make financial progress feel possible for more people, in more places.</h2><p>We build enduring relationships around a simple idea: banking should help people see clearly, act confidently, and feel supported through every chapter.</p><div><b>190+</b><small>Markets connected</small><b>24/7</b><small>Human care</small><b>11</b><small>Specialist services</small></div><Link to="/register" className="button button-dark">Build with Core <ArrowRight /></Link></div>}</div>;
}
