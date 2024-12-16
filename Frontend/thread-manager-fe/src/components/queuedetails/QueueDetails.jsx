import React, { useEffect, useState } from "react";

const QueueDetails = () => {
  const [queue, setQueue] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/api/threads/queue-stream");

    // "queue-update" eventini dinler
    eventSource.addEventListener("queue-update", (event) => {
      const queueData = JSON.parse(event.data); // Backend'den gelen JSON'u parse et
      setQueue(queueData); // Gelen kuyruk bilgilerini state'e yaz
    });

    eventSource.onerror = () => {
      console.error("SSE bağlantı hatası");
      eventSource.close(); // Bağlantı hatası durumunda SSE'yi kapat
    };

    return () => {
      eventSource.close(); // Component unmount edildiğinde bağlantıyı kapat
    };
  }, []);

  return (
    <div>
      <h1>Queue State</h1>
      <ul>
        {queue.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    </div>
  );
};

export default QueueDetails;
