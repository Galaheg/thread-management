// App.jsx

import React from "react";
import AddThreads from "./components/AddThread/AddThread";
import ControlThreads from "./components/ControlThread/ControlThread";
import PriorityControl from "./components/PriorityControl/PriorityControl";

const App = () => {
  return (
    <div>
      <AddThreads />
      <ControlThreads />
      <PriorityControl/>
    </div>
  );
};

export default App;