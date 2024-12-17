import axios from "axios";

const BASE_URL = "http://localhost:8081/api/receivers";

export const createReceivers = async (count, isPriorityChangeable) => {
  try {
    const response = await axios.post(`${BASE_URL}/add-receivers`, null, {
      params: { count, isPriorityChangeable },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const startAllReceiverThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/start-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopAllReceiverThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartAllReceiverThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const startReceiverThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/start-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopReceiverThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartReceiverThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const changeReceiverThreadPriority = async (index, priority) => {
  try {
    const response = await axios.post(`${BASE_URL}/change-thread-priority?index=${index}&priority=${priority}`, null);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};


