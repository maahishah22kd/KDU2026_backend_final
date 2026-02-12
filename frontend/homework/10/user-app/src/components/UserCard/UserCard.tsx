import type { User } from "../../types/user";
import styles from "./UserCard.module.scss";

type UserCardProps = {
  user: User;
  onClick: () => void;
};

export default function UserCard({ user, onClick }: UserCardProps) {
  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <img
        className={styles.img}
        src={(user as any).image}
        alt={user.firstName}
      />
      <div className={styles.info}>
        <div className={styles.title}>
          {user.firstName} {user.lastName}
        </div>
        <div className={styles.meta}>{user.email}</div>
        <div className={styles.meta}>{user.phone}</div>
        <div className={styles.meta}>Age: {user.age}</div>
      </div>
    </button>
  );
}
