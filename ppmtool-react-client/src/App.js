import "./App.css";
import Dashboard from "./components/Dashboard";
import Header from "./components/layout/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AddProject from "./components/project/AddProject";
import { Provider } from "react-redux";
import store from "./store";
import UpdateProject from "./components/project/UpdateProject";
import ProjectBoard from "./components/projectBoard/ProjectBoard";
import AddProjectTask from "./components/projectBoard/projectTask/AddProjectTask";
import UpdateProjectTask from "./components/projectBoard/projectTask/UpdateProjectTask";
import Landing from "./components/layout/Landing";
import Register from "./components/userManagement/Register";
import Login from "./components/userManagement/Login";
import jwt_decode from "jwt-decode";
import setJWToken from "./securityUtils/setJWToken";
import { Component } from "react";
import { SET_CURRENT_USER } from "./actions/types";
import { logout } from "./actions/securityActions";
import SecuredRoute from "./securityUtils/secureRoute";

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setJWToken(jwtToken);
  const decoded_jwtToken = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decoded_jwtToken,
  });

  const currentTime = Date.now() / 1000;
  if (decoded_jwtToken.exp < currentTime) {
    store.dispatch(logout());
    window.location.href = "/";
  }
}

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Router>
          <div className="App">
            <Header></Header>
            <Route exact path="/" component={Landing}></Route>
            <Route exact path="/login" component={Login}></Route>
            <Route exact path="/register" component={Register}></Route>

            <Switch>
              <SecuredRoute
                exact
                path="/dashboard"
                component={Dashboard}
              ></SecuredRoute>
              <SecuredRoute
                exact
                path="/addProject"
                component={AddProject}
              ></SecuredRoute>
              <SecuredRoute
                exact
                path="/updateProject/:id"
                component={UpdateProject}
              ></SecuredRoute>
              <SecuredRoute
                exact
                path="/projectBoard/:id"
                component={ProjectBoard}
              ></SecuredRoute>
              <SecuredRoute
                exact
                path="/addProjectTask/:id"
                component={AddProjectTask}
              ></SecuredRoute>
              <SecuredRoute
                exact
                path="/updateProjectTask/:backlog_id/:pt_id"
                component={UpdateProjectTask}
              ></SecuredRoute>
            </Switch>
          </div>
        </Router>
      </Provider>
    );
  }
}

export default App;
