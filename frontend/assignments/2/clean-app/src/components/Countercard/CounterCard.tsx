import styles from "./CounterCard.module.scss";

type CounterCardProps={
  readonly icon:string;
  readonly label:string;
  readonly min?:number;
  readonly max?:number;
  readonly value:number;
  readonly compact?:boolean;   
  readonly onChange:(next:number)=>void;
};

export default function CounterCard({
  icon,
  label,
  value,
  onChange,
  min=0,
  max=99,
  compact= false, 
}:CounterCardProps){
    const dec=()=>onChange(Math.max(min,value-1));
    const inc=()=>onChange(Math.min(max,value+1));

    return(
        <div className={styles.card}>
     {!compact && (
        <div className={styles.card__top}>
          {icon && <img className={styles.card__icon} src={icon} alt={label} />}
          {label && <div className={styles.card__label}>{label}</div>}
        </div>
      )}

      <div className={styles.card__controls}>
        <button
          type="button"
          className={styles.card__btn}
          onClick={dec}
          disabled={value<=min}
        >-
        </button>
        <div className={styles.card__value}>{value}</div>
        <button
          type="button"
          className={styles.card__btn}
          onClick={inc}
          disabled={value>=max}
        >+
        </button>
      </div>
    </div>
    );
}