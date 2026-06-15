import { ArrowRight, Check, ChevronRight, MoveUpRight } from "lucide-react";
import { motion } from "framer-motion";
import { Link, useParams } from "react-router-dom";
import { BankCard } from "../components/BankCard";
import { products } from "../data";

export function ProductPage({ slug }: { slug?: string }) {
  const params = useParams();
  const product = products.find(p => p.slug === (slug || params.slug)) || products[0];
  const Icon = product.icon;
  return (
    <div className="product-page">
      <section className="product-hero">
        <div className="product-hero-copy">
          <div className="breadcrumb"><Link to="/">Core</Link><ChevronRight size={14} /><span>{product.eyebrow}</span></div>
          <motion.div initial={{ opacity: 0, y: 25 }} animate={{ opacity: 1, y: 0 }}>
            <span className="eyebrow"><i />{product.eyebrow}</span>
            <h1>{product.title}</h1><p>{product.description}</p>
            <div className="hero-actions"><Link to="/register" className="button button-gold">Get started <ArrowRight size={17} /></Link><Link to="/contact" className="button button-ghost">Talk to us <MoveUpRight size={16} /></Link></div>
          </motion.div>
        </div>
        <motion.div className="product-hero-art" initial={{ opacity: 0, x: 40 }} animate={{ opacity: 1, x: 0 }} transition={{ duration: .8 }}>
          <div className="product-sun" style={{ background: product.accent }} /><Icon size={130} strokeWidth={.6} />
          {product.slug === "credit-cards" ? <BankCard platinum /> : <div className="product-metric"><small>{product.metricLabel}</small><b>{product.metric}</b><span>Distinctive value, designed around you.</span></div>}
        </motion.div>
      </section>
      <section className="product-benefits">
        <div><span className="section-kicker">The Core difference</span><h2>More clarity.<br /><em>More possibility.</em></h2></div>
        <div className="benefit-grid">{product.benefits.map((benefit, i) => <motion.article key={benefit} initial={{ opacity: 0, y: 25 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }} transition={{ delay: i * .1 }}><span>0{i + 1}</span><Check /><h3>{benefit}</h3><p>Thoughtfully designed tools and responsive support make this benefit work naturally around your day.</p></motion.article>)}</div>
      </section>
      <section className="product-feature">
        <div className="feature-art"><div className="feature-screen"><span>YOUR FINANCIAL PICTURE</span><b>$84,290.80</b><div className="line-art"><i /><i /><i /><i /><i /><i /><i /></div><small>+8.2% this quarter</small></div></div>
        <div><span className="section-kicker">Intelligence, included</span><h2>See more.<br /><em>Decide better.</em></h2><p>Live context and useful guidance help turn financial information into confident next steps.</p><ul>{product.benefits.map(x => <li key={x}><Check />{x}</li>)}</ul><Link to="/register" className="dark-link">Open your account <ArrowRight size={16} /></Link></div>
      </section>
    </div>
  );
}

