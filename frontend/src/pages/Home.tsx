import { ArrowRight, Check, Globe2, LockKeyhole, MoveUpRight, ShieldCheck, Sparkles, Zap } from "lucide-react";
import { motion, useScroll, useTransform } from "framer-motion";
import { Link } from "react-router-dom";
import { BankCard } from "../components/BankCard";
import { Globe } from "../components/Globe";
import { products } from "../data";

const rise = { initial: { opacity: 0, y: 30 }, whileInView: { opacity: 1, y: 0 }, viewport: { once: true, margin: "-80px" }, transition: { duration: .7 } };

export function Home() {
  const { scrollYProgress } = useScroll();
  const cardY = useTransform(scrollYProgress, [0, .4], [0, -90]);
  return (
    <>
      <section className="hero">
        <div className="hero-noise" />
        <div className="hero-orbit orbit-one" /><div className="hero-orbit orbit-two" />
        <div className="hero-content">
          <motion.div className="eyebrow" initial={{ opacity: 0 }} animate={{ opacity: 1 }}><span /> Banking, elevated by intelligence</motion.div>
          <motion.h1 initial={{ opacity: 0, y: 25 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: .1, duration: .8 }}>Your world.<br /><em>In better balance.</em></motion.h1>
          <motion.p initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: .25 }}>A global financial partner designed around your life, with human insight and digital precision at every turn.</motion.p>
          <motion.div className="hero-actions" initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: .35 }}>
            <Link to="/register" className="button button-gold">Open an account <ArrowRight size={17} /></Link>
            <Link to="/personal" className="button button-ghost">Explore Core <MoveUpRight size={16} /></Link>
          </motion.div>
          <motion.div className="hero-proof" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ delay: .6 }}><span><ShieldCheck size={16} /> Bank-grade security</span><span><Globe2 size={16} /> 190+ markets</span><span><Zap size={16} /> Real-time movement</span></motion.div>
        </div>
        <motion.div className="hero-card-stack" style={{ y: cardY }}>
          <div className="halo" />
          <BankCard />
          <BankCard platinum className="back-card" />
          <div className="floating-metric metric-one"><small>Global access</small><b>190+ <span>markets</span></b></div>
          <div className="floating-metric metric-two"><span className="pulse" /><small>Transfer complete</small><b>+$2,480.00</b></div>
        </motion.div>
        <div className="scroll-cue"><span /> Scroll to discover</div>
      </section>

      <section className="trust-strip">
        <span>Built for confidence</span><b>256-bit encryption</b><i /><b>Real-time fraud intelligence</b><i /><b>24/7 human care</b><i /><b>Global access</b>
      </section>

      <section className="intro-section">
        <motion.div {...rise}><span className="section-kicker">One relationship. Every ambition.</span><h2>A more considered way to <em>bank.</em></h2></motion.div>
        <motion.p {...rise}>From the everyday to the once-in-a-lifetime, Core brings your finances into focus with beautifully connected products and guidance that sees the whole picture.</motion.p>
      </section>

      <section className="product-grid">
        {products.slice(0, 6).map((product, index) => (
          <motion.div className={`product-tile tile-${index + 1}`} key={product.slug} {...rise}>
            <div className="product-icon" style={{ color: product.accent }}><product.icon size={23} /></div>
            <span>{product.eyebrow}</span><h3>{product.title}</h3><p>{product.description}</p>
            <Link to={`/${product.slug}`}>Explore <ArrowRight size={15} /></Link>
            {index === 0 && <div className="balance-art"><small>CORE RESERVE</small><b>$42,680<sup>.30</sup></b><div className="mini-bars">{[30, 55, 42, 78, 63, 90, 76].map((h, i) => <i key={i} style={{ height: `${h}%` }} />)}</div></div>}
            {index === 1 && <BankCard platinum small />}
          </motion.div>
        ))}
      </section>

      <section className="app-story">
        <div className="app-copy">
          <motion.div {...rise}><span className="section-kicker light">The Core app</span><h2>Everything in hand.<br /><em>Nothing in the way.</em></h2><p>See the full picture, move money instantly, and receive timely guidance from one thoughtfully designed experience.</p></motion.div>
          <div className="feature-list">
            {[["01", "Know where you stand", "Live balances and intelligent spending context."], ["02", "Move with confidence", "Fast transfers with built-in fraud intelligence."], ["03", "Stay one step ahead", "Useful insights before you need to ask."]].map(([n, title, text]) => <motion.div key={n} {...rise}><b>{n}</b><span><strong>{title}</strong><small>{text}</small></span></motion.div>)}
          </div>
          <Link to="/register" className="button button-light">Experience Core <ArrowRight size={17} /></Link>
        </div>
        <motion.div className="phone-scene" initial={{ opacity: 0, rotateY: 25, y: 60 }} whileInView={{ opacity: 1, rotateY: -8, y: 0 }} viewport={{ once: true }} transition={{ duration: 1 }}>
          <div className="phone-halo" /><img src="/app-preview.svg" alt="Core mobile banking app interface" />
          <div className="phone-float pf-one"><Sparkles size={18} /><span><small>CORE INSIGHT</small>Spending is 12% below plan</span></div>
          <div className="phone-float pf-two"><Check size={18} /><span><small>TRANSFER COMPLETE</small>Arrived instantly</span></div>
        </motion.div>
      </section>

      <section className="global-section">
        <Globe />
        <div className="global-copy"><span className="section-kicker light">A bank without borders</span><h2>At home,<br /><em>everywhere.</em></h2><p>One trusted relationship connecting your financial life across markets, currencies, and ambitions.</p><div className="global-stats"><span><b>190+</b><small>Markets</small></span><span><b>24/7</b><small>Global care</small></span><span><b>42</b><small>Currencies</small></span></div><Link to="/about" className="button button-ghost">Our global presence <ArrowRight size={16} /></Link></div>
      </section>

      <section className="security-section">
        <motion.div className="security-visual" {...rise}><div className="shield-ring ring-a" /><div className="shield-ring ring-b" /><div className="shield-core"><LockKeyhole size={38} /></div><span className="secure-tag st-a"><i /> Session protected</span><span className="secure-tag st-b"><i /> Identity verified</span></motion.div>
        <motion.div className="security-copy" {...rise}><span className="section-kicker">Confidence by design</span><h2>Quietly working.<br /><em>Always protecting.</em></h2><p>Layered security, continuous intelligence, and complete control help keep your financial life yours.</p><ul><li><Check /> Adaptive fraud intelligence</li><li><Check /> Instant card controls</li><li><Check /> Privacy-first architecture</li></ul><Link to="/customer-care" className="dark-link">Explore security <ArrowRight size={16} /></Link></motion.div>
      </section>

      <section className="final-cta"><div className="cta-glow" /><span>Begin with Core</span><h2>Make room for<br /><em>what’s next.</em></h2><p>Open an account in minutes. Build a relationship for years.</p><Link to="/register" className="button button-gold">Become a client <ArrowRight size={17} /></Link></section>
    </>
  );
}
