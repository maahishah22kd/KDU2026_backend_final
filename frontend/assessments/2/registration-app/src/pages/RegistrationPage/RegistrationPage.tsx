import styles from "./RegistrationPage.module.scss";
import type {RootState,AppDispatch} from "../../app/store";
import {useDispatch,useSelector} from "react-redux";
import { setEmail,setEvent, setName, setMessage } from "../../features/registration/registrationSlice";
import { submitRegistrationThunk } from "../../features/registration/registrationsThunk";
import {useNavigate} from "react-router-dom";
import { useEffect } from "react";

export default function RegistrationPage(){
    const dispatch = useDispatch<AppDispatch>();
  const form = useSelector((state: RootState)=> state.register.form);
    const submitStatus= useSelector((state: RootState)=>state.register.submitStatus);
  const isSubmitting=submitStatus==="loading";
  const navigate=useNavigate();

    useEffect(() => {
  if (submitStatus==="succeeded") {
    navigate("/confirmation");
  }
}, [submitStatus,navigate]);

    return(
        <div className={styles.register}>
            
            <div className={styles.register__heading}>
                Event Registration
            </div>

            <div className={styles.register__fields}>
                Name*
            </div>
            <div className={styles.register__mssg}>
                <input placeholder="Enter your full name" required type="text"
                value={form.name}
                onChange={(e) =>
                  dispatch(setName(e.target.value ))
                }
                >
                </input>
            </div>

            <div className={styles.register__fields}>
                Email*
            </div>
            <div className={styles.register__mssg}>
                <input placeholder="your.email@example.com" required type="email" 
                value={form.email}
                onChange={(e)=>dispatch(setEmail(e.target.value))}
                >
                </input>
            </div>

            <div className={styles.register__fields}>
                Select Event*
            </div>
            <div className={styles.register__mssg}>
                <select 
                value={form.events}
                onChange={(e)=>dispatch(setEvent(e.target.value))}
                >
                <option value="sports">Sports</option>
                <option value="dance">Dance</option>
                <option value="music">Music</option>
                <option value="quiz">Quiz</option>
                </select>
            </div>

            <div className={styles.register__fields}>
                Message*
            </div>
            <textarea className={styles.register__mssg}
            value={form.message}
            onChange={(e)=>dispatch(setMessage(e.target.value))}
            >
                </textarea>

            <div className={styles.register__submit}>
                <button type="button"
                disabled={isSubmitting}
                 onClick={()=>{
                  dispatch(submitRegistrationThunk(form))}}>
                Register for Event
                </button>
            </div>
            <p>* Required Fields</p>
        </div>
    );
}