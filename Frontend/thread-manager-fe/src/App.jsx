import React from "react";
import AddThreads from "./components/addthread/AddThread";
import ControlThread from "./components/controlthread/ControlThread";
import PriorityControl from "./components/prioritycontrol/PriorityControl";
import ThreadDetails from "./components/threaddetails/ThreadDetails"

const App = () => {
  return (
    <div>
      <AddThreads />
      <ControlThread />
      <PriorityControl/>
      <ThreadDetails/>
    </div>
  );
};

export default App;