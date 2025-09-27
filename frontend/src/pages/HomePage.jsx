import React, {useEffect, useState} from 'react'
import {profil, list, advice, goals, addActivity, statistics, accountMenu, accountCross, notification} from '../assets'
import Card from '../components/Card'
import { Link, useNavigate } from "react-router-dom";
import AccountNavbar from '../components/AccountNavbar'

const HomePage = () => {
  
  return (
    <div className="h-min-screen ">
      <AccountNavbar/>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 p-6 ">
          <Card image={addActivity} title="Add activity" href="/addActivity"/>
          <Card image={list} title="Activities list" href="/activities"/>
          <Card image={statistics} title="Statistics" href="/statistics"/>
          <Card image={advice} title="Advices" href="/advices"/>
          <Card image={goals} title="Goals" href="/goals"/>
      </div>

    </div>
  )
}

export default HomePage
