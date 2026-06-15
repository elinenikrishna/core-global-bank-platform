import { motion } from "framer-motion";
import { Wifi } from "lucide-react";

export function BankCard({ platinum = false, small = false, className = "" }: { platinum?: boolean; small?: boolean; className?: string }) {
  return (
    <motion.div
      className={`bank-card ${platinum ? "platinum" : "black"} ${small ? "small" : ""} ${className}`}
      whileHover={{ rotateY: -8, rotateX: 5, scale: 1.025 }}
      transition={{ type: "spring", stiffness: 180, damping: 18 }}
    >
      <div className="card-shine" />
      <div className="card-top"><span className="card-core">CORE<span>+</span></span><Wifi size={22} /></div>
      <div className="card-chip"><i /><i /><i /><i /></div>
      <div className="card-number">4921&nbsp; •••• &nbsp;••••&nbsp; 4680</div>
      <div className="card-bottom"><span><small>CARDHOLDER</small>AVERY MORGAN</span><span><small>VALID THRU</small>08/30</span><b>VISA</b></div>
    </motion.div>
  );
}

