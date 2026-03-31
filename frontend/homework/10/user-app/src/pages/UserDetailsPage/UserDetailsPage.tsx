import { useNavigate, useParams } from "react-router-dom";
import { useCallback, useRef } from "react";
import { useGetUserByIdQuery } from "../../features/users/usersSlice";
import styles from "./UserDetailsPage.module.scss";

export default function UserDetailPage() {
  const {id}=useParams();
  const userId=id ?? "";

  const { data: user, isLoading, isFetching } = useGetUserByIdQuery(userId, {
    skip: !userId,
  });

  const navigate = useNavigate();
  const handleBack = useCallback(() => navigate(-1), [navigate]);

  const initialHadCachedData = useRef<boolean | null>(null);

  if (initialHadCachedData.current === null && userId) {
    initialHadCachedData.current = !isLoading;
  }

  const badgeText =!user? ""
      : initialHadCachedData.current
        ? "Using cached data ✓"
        : (isFetching || isLoading)
          ? "Fetching…"
          : "Freshly fetched";

  return (
    <div className={styles.page}>
      <div className={styles.wrapper}>
        <button type="button" className={styles.back} onClick={handleBack}>
          ← Back
        </button>

        {user && <span className={styles.badge}>{badgeText}</span>}

        {isLoading && <div>Loading...</div>}

        {user && (
          <div className={styles.card}>
            <div className={styles.grid}>
              <div className={styles.imageBox}>
                <img
                  src={user.image}
                  alt={`${user.firstName} ${user.lastName}`}
                  className={styles.image}
                />
              </div>

              <div>
                <h1 className={styles.title}>
                  {user.firstName} {user.lastName}
                </h1>

                <div className={styles.meta}>
                  <div>
                    <span>Email:</span> {user.email}
                  </div>
                  <div>
                    <span>Phone:</span> {user.phone}
                  </div>
                  <div>
                    <span>Age:</span> {user.age}
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
