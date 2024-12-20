import React from "react";
import AddThreads from "../components/addthread/AddThread";
import ControlThread from "../components/controlthread/ControlThread";
import PriorityControl from "../components/prioritycontrol/PriorityControl";
import ThreadDetails from "../components/threaddetails/ThreadDetails";
import QueueDetails from "../components/queuedetails/QueueDetails";
import styles from './Home.module.css';

const App = () => {
  return (
    <div className={styles.container}>
      <div className={styles.leftPanel}>
        <AddThreads />
        <hr></hr>
        <ControlThread />
        <hr></hr>
        <PriorityControl />
      </div>
      <div className={styles.rightPanel}>
        <ThreadDetails />
        <QueueDetails />
      </div>
    </div>
  );
};

export default App;
