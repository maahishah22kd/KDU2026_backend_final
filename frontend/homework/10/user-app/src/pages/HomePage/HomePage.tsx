import UserCard from "../../components/UserCard";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./HomePage.module.scss";
import { useGetUsersQuery, useAddUserMutation } from "../../features/users/usersSlice";

type NewUserForm = {
  firstName: string;
  lastName: string;
  email: string;
  age: string;
};

export default function HomePage() {
  const navigate = useNavigate();

  const { data, error, isLoading, refetch, isFetching } = useGetUsersQuery();
  const [addUser, { isLoading: isSubmitting, error: submitError }] = useAddUserMutation();

  const [form, setForm] = useState<NewUserForm>({
    firstName: "",
    lastName: "",
    email: "",
    age: "",
  });

  const users = data?.users ?? [];

  const handleCardClick = useCallback(
    (id: number) => navigate(`/user/${id}`),
    [navigate]
  );

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const firstName = form.firstName.trim();
    const lastName = form.lastName.trim();
    const email = form.email.trim();
    const ageNum = Number(form.age);

    if (!firstName || !lastName || !email || !Number.isFinite(ageNum) || ageNum <= 0) {
      alert("Please fill all fields correctly.");
      return;
    }

    try {
      await addUser({ firstName, lastName, email, age: ageNum }).unwrap();
      setForm({ firstName: "", lastName: "", email: "", age: "" });
    }
    catch(err){
      console.log("Failed to add user",err);
    }
  };

  return (
    <div className={styles.home}>
      <h1 className={styles.title}>User Directory</h1>

      <div className={styles.inner}>
        <section className={styles.panel}>
          <h2 className={styles.panelTitle}>Add New User</h2>

          <form className={styles.form} onSubmit={onSubmit}>
            <div className={styles.formRow}>
              <input
                className={styles.input}
                placeholder="First name"
                value={form.firstName}
                onChange={(e) => setForm((p) => ({ ...p, firstName: e.target.value }))}
              />

              <input
                className={styles.input}
                placeholder="Last name"
                value={form.lastName}
                onChange={(e) => setForm((p) => ({ ...p, lastName: e.target.value }))}
              />

              <input
                className={styles.input}
                placeholder="Email"
                value={form.email}
                onChange={(e) => setForm((p) => ({ ...p, email: e.target.value }))}
              />

              <input
                className={styles.input}
                placeholder="Age"
                type="number"
                value={form.age}
                onChange={(e) => setForm((p) => ({ ...p, age: e.target.value }))}
              />
            </div>

            <button className={styles.btn} type="submit">
              {isSubmitting ? "Adding..." : "Add User"}
            </button>

            {submitError && (
              <div className={styles.stateError} style={{ marginTop: 10 }}>
                Failed to add user. Please try again.
              </div>
            )}
          </form>
        </section>

        <section className={styles.panel}>
          <div className={styles.panelHeader}>
            <h2 className={styles.panelTitle}>Users ({users.length})</h2>

            <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
              {isFetching && !isLoading && <span>Refreshing...</span>}

              {error && (
                <button className={styles.linkBtn} type="button" onClick={() => refetch()}>
                  Retry
                </button>
              )}
            </div>
          </div>

          {isLoading && <div className={styles.state}>Loading...</div>}
          {!isLoading && error && (
            <div className={styles.stateError}>Failed to load users</div>
          )}

          {!isLoading && !error && (
            <div className={styles.grid}>
              {users.map((u) => (
                <UserCard key={u.id} user={u} onClick={() => handleCardClick(u.id)} />
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
