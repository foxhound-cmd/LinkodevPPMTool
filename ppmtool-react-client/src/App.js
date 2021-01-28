import "./App.css";
import Dashboard from "./components/Dashboard";
import Header from "./components/layout/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route } from "react-router-dom";
import AddProject from "./components/project/AddProject";
import { Provider } from "react-redux";
import store from "./store";
import UpdateProject from "./components/project/UpdateProject";
import ProjectBoard from "./components/projectBoard/ProjectBoard";
import AddProjectTask from "./components/projectBoard/projectTask/AddProjectTask";

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Header></Header>
          <Route exact path="/dashboard" component={Dashboard}></Route>
          <Route exact path="/addProject" component={AddProject}></Route>
          <Route
            exact
            path="/updateProject/:id"
            component={UpdateProject}
          ></Route>
          <Route
            exact
            path="/projectBoard/:id"
            component={ProjectBoard}
          ></Route>
          <Route
            exact
            path="/addProjectTask/:id"
            component={AddProjectTask}
          ></Route>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
