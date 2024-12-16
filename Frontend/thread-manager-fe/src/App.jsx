import React from "react";
import AddThreads from "./components/addthread/AddThread";
import ControlThread from "./components/controlthread/ControlThread";
import PriorityControl from "./components/prioritycontrol/PriorityControl";
import ThreadDetails from "./components/threaddetails/ThreadDetails"
import QueueDetails from "./components/queuedetails/QueueDetails"

const App = () => {
  return (
    <div>
      <AddThreads />
      <ControlThread />
      <PriorityControl/>
      <ThreadDetails/>
      <QueueDetails/>
    </div>
  );
};

export default App;