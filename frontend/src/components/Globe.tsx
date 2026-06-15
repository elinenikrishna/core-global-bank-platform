import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, Stars } from "@react-three/drei";
import { useRef } from "react";
import * as THREE from "three";

function WireGlobe() {
  const ref = useRef<THREE.Group>(null);
  useFrame((_, delta) => {
    if (ref.current) ref.current.rotation.y += delta * 0.08;
  });
  const points = [
    [-1.45, .45, .55], [-.35, .9, 1.42], [.82, .55, 1.18], [1.52, -.3, .2],
    [-.7, -.9, 1.1], [.45, -.15, 1.6], [1.15, .95, .62], [-1.15, -.65, -.92]
  ];
  return (
    <group ref={ref}>
      <mesh>
        <sphereGeometry args={[1.7, 48, 48]} />
        <meshStandardMaterial color="#0b241e" emissive="#07110f" roughness={.65} metalness={.2} />
      </mesh>
      <mesh>
        <sphereGeometry args={[1.715, 24, 16]} />
        <meshBasicMaterial color="#5c927f" wireframe transparent opacity={.16} />
      </mesh>
      {points.map((p, index) => <mesh key={index} position={p as [number, number, number]}><sphereGeometry args={[.035, 12, 12]} /><meshBasicMaterial color={index % 2 ? "#c8a55a" : "#7ce2bd"} /></mesh>)}
      <pointLight position={[2, 2, 3]} intensity={22} color="#c8a55a" />
      <pointLight position={[-2, -1, 2]} intensity={16} color="#7ce2bd" />
    </group>
  );
}

export function Globe() {
  return <div className="globe"><Canvas camera={{ position: [0, 0, 5.5], fov: 45 }}><ambientLight intensity={.6} /><Stars radius={50} depth={20} count={700} factor={2} fade /><WireGlobe /><OrbitControls enableZoom={false} enablePan={false} autoRotate autoRotateSpeed={.35} /></Canvas></div>;
}

