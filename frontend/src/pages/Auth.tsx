import { ArrowRight, Check, Eye, LockKeyhole, Mail, ShieldCheck, User } from "lucide-react";
import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Brand } from "../components/Brand";

export function AuthPage({ register = false }: { register?: boolean }) {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const submit = (e: FormEvent) => { e.preventDefault(); setLoading(true); setTimeout(() => navigate("/dashboard"), 650); };
  return (
    <div className="auth-page">
      <div className="auth-art">
        <Brand />
        <div className="auth-art-copy"><span className="section-kicker light">Your secure financial home</span><h1>{register ? <>Banking that moves<br /><em>with your life.</em></> : <>Welcome back<br /><em>to Core.</em></>}</h1><p>Clarity, control, and confidence across your complete financial world.</p><ul><li><Check />Continuous fraud monitoring</li><li><Check />Secure biometric access</li><li><Check />Private by design</li></ul></div>
        <div className="auth-orbit ao-one" /><div className="auth-orbit ao-two" /><div className="auth-shield"><ShieldCheck size={38} /></div>
      </div>
      <div className="auth-form-wrap">
        <div className="auth-mobile-brand"><Brand /></div>
        <form onSubmit={submit}>
          <div className="auth-title"><span>{register ? "Become a client" : "Secure sign in"}</span><h2>{register ? "Create your Core account" : "Good to see you."}</h2><p>{register ? "It takes about three minutes." : "Enter your details to access your accounts."}</p></div>
          {register && <label><span>Full name</span><div><User size={17} /><input required placeholder="Avery Morgan" /></div></label>}
          <label><span>Email address</span><div><Mail size={17} /><input type="email" required defaultValue={register ? "" : "avery@coreglobal.demo"} placeholder="you@email.com" /></div></label>
          <label><span>Password</span><div><LockKeyhole size={17} /><input type="password" required defaultValue={register ? "" : "CoreDemo2026!"} placeholder="Minimum 8 characters" /><Eye size={17} /></div></label>
          {!register && <div className="form-meta"><label className="checkbox"><input type="checkbox" defaultChecked />Remember this device</label><a>Forgot password?</a></div>}
          {register && <label className="checkbox terms"><input type="checkbox" required />I agree to the digital banking terms and privacy notice.</label>}
          <button className="button button-dark auth-submit" disabled={loading}>{loading ? <span className="spinner" /> : <>{register ? "Create account" : "Sign in securely"}<ArrowRight size={17} /></>}</button>
          <div className="auth-security"><ShieldCheck size={17} /> Protected by 256-bit encryption</div>
          <p className="auth-switch">{register ? "Already a Core client?" : "New to Core?"} <Link to={register ? "/login" : "/register"}>{register ? "Sign in" : "Open an account"}</Link></p>
        </form>
      </div>
    </div>
  );
}
