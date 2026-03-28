import { useDispatch, useSelector } from "react-redux";
import type { AppDispatch, RootState } from "../../app/store";
import styles from "./RegistrationConfirmationPage.module.scss";
import { useEffect } from "react";
import { fetchStatusThunk } from "../../features/confirmation/confirmationThunk";

export default function RegistrationConfirmationPage(){
    const form = useSelector((state: RootState)=> state.register.form);
    const submitStatus=useSelector((state:RootState)=>state.register.submitStatus);
    const submitError=useSelector((state:RootState)=>state.register.submitError);
    const confirmation=useSelector((state:RootState)=>state.register.confirmation);
    const dispatch = useDispatch<AppDispatch>();
    if(submitStatus==="failed") return <div>{submitError??"Booking failed"}</div>

    const confirmStatus= useSelector((state:RootState)=> state.confirmation);
    useEffect(() => {
      if (submitStatus==="queued") {
      dispatch(fetchStatusThunk());
      }
    }, [submitStatus]);
    return(
        <div className={styles.confirm__mssg}>
        <h2>REGISTRATION CONFIRMED</h2>

        <div className={styles.book__summaryBody}>
        <div className={styles.book__summaryRow}>
          <span>Booking ID</span>
          <span>{confirmation?.registrationId ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Name</span>
          <span>{ form.name ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Email</span>
          <span>{form.email ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Event</span>
          <span>{form.events}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Message</span>
          <span>{form.message}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Status</span>
          <span>{confirmStatus.submitStatus}</span>
        </div>
      </div>
    </div>
    );
}