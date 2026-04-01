import styles from "./Navbar.module.scss";

export default function Navbar(){

    return(
        <div className={styles.header}>
            <div className={styles.header__inner}>
                <div className={styles.header__logo}>
                    <span className={styles.header__clean}>Clean</span>
                    <span className={styles.header__ly}>ly</span>
                </div>
                <span className={styles.header__number}>800-710-8420</span>
            </div>
        </div>
    );
}