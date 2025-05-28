<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main</title>
    <style>
        body {
            margin: 0;
            font-family: sans-serif;
            background-color: #f8f9fa;
            text-align: center;
        }
        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 30px;
            background-color: #343a40;
            color: white;
        }
        .logo-nav {
            display: flex;
            align-items: center;
        }
        .logo-nav img {
            height: 40px;
            margin-right: 15px;
        }
        nav {
            position: relative;
        }
        nav:hover .dropdown {
            display: block;
        }
        .nav-link {
            cursor: pointer;
            color: white;
            margin-right: 20px;
        }
        .dropdown {
            display: none;
            position: absolute;
            top: 25px;
            background-color: white;
            color: black;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            z-index: 999;
        }
        .dropdown a {
            display: block;
            padding: 8px 12px;
            text-decoration: none;
            color: black;
        }
        .dropdown a:hover {
            background-color: #f1f1f1;
        }
        .auth-buttons a {
            color: white;
            text-decoration: none;
            margin-left: 15px;
            padding: 6px 12px;
            border: 1px solid white;
            border-radius: 4px;
        }
        .auth-buttons a:hover {
            background-color: #495057;
        }
        main {
            margin-top: 100px;
        }
        h1 {
            font-size: 48px;
            color: #343a40;
        }
    </style>
</head>
<body>

<header>
    <div class="logo-nav">
        <img src="<%= request.getContextPath() %>/resources/img/btc-logo.png" alt="Logo">
        <nav>
            <span class="nav-link">거래하기 ▾</span>
            <div class="dropdown">
                <a href="HOME.jsp">BTC</a>
            </div>
        </nav>
    </div>
    <div class="auth-buttons">
        <a href="signin.jsp">SIGNIN</a>
        <a href="regist.jsp">REGIST</a>
    </div>
</header>

<main>
    <h1>BTC MOCK INVEST</h1>
</main>

</body>
</html>
