import React from "react";
import styles from './SQLInfo.module.css'; // Kendi CSS dosyan

const About = () => {
  return (
    <div className={styles.aboutPage}>
      <h1>About</h1>
      <p>This is the Thread Manager application. Here you can add, manage, and control threads efficiently.</p>
    </div>
  );
};

export default About;
